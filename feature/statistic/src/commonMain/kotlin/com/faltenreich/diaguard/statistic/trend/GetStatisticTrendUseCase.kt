package com.faltenreich.diaguard.statistic.trend

import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.property.MeasurementValueRange
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueTintMapper
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.DateRangeProgression
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.statistic.daterange.StatisticDateRangeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.math.max

class GetStatisticTrendUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val getValueTint: MeasurementValueTintMapper,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(
        property: MeasurementProperty.Local?,
        dateRange: DateRange,
        dateRangeType: StatisticDateRangeType,
    ): Flow<StatisticTrendState> {
        val dateRangeProgression = DateRangeProgression(
            dateRange = dateRange,
            intervalDateUnit = dateRangeType.intervalDateUnit,
        )
        return if (property == null) {
            getPlaceholderState(dateRangeProgression, dateRangeType)
        } else {
            getValueState(property, dateRangeProgression, dateRangeType)
        }
    }

    private fun getPlaceholderState(
        dateRangeProgression: DateRangeProgression,
        dateRangeType: StatisticDateRangeType,
    ): Flow<StatisticTrendState> {
        return flowOf(
            StatisticTrendState(
                intervals = dateRangeProgression.map { intervalDateRange ->
                    StatisticTrendState.Interval(
                        dateRange = intervalDateRange,
                        label = getLabel(intervalDateRange, dateRangeType),
                        average = null,
                    )
                },
                targetValue = MeasurementValueRange.BLOOD_SUGAR_TARGET_DEFAULT,
                maximumValue = MeasurementValueRange.BLOOD_SUGAR_MAXIMUM_DEFAULT,
            )
        )
    }

    private fun getValueState(
        property: MeasurementProperty.Local,
        dateRangeProgression: DateRangeProgression,
        dateRangeType: StatisticDateRangeType,
    ): Flow<StatisticTrendState> {
        return combine(
            dateRangeProgression.map { intervalDateRange ->
                getAverageValue(property, intervalDateRange).map { intervalDateRange to it }
            }
        ) { averagesByInterval ->
            averagesByInterval.map { (intervalDateRange, average) ->
                StatisticTrendState.Interval(
                    dateRange = intervalDateRange,
                    label = getLabel(intervalDateRange, dateRangeType),
                    average = average?.let {
                        val value = MeasurementValue.Average(
                            value = average,
                            property = property,
                        )
                        StatisticTrendState.Value(
                            value = average,
                            tint = getValueTint(value),
                        )
                    },
                )
            }
        }.map { days ->
            val targetValue = property.range.target ?: MeasurementValueRange.BLOOD_SUGAR_TARGET_DEFAULT
            val maximumValue = days.mapNotNull { it.average?.value }.maxOrNull()
            val maximumValueDefault = targetValue * 2
            StatisticTrendState(
                intervals = days,
                targetValue = targetValue,
                maximumValue = max(maximumValue ?: maximumValueDefault, maximumValueDefault),
            )
        }
    }

    private fun getAverageValue(
        property: MeasurementProperty.Local,
        dateRange: DateRange,
    ): Flow<Double?> {
        return valueRepository.observeAverageByPropertyId(
            propertyId = property.id,
            minDateTime = dateRange.start.atStartOfDay(),
            maxDateTime = dateRange.endInclusive.atEndOfDay(),
        )
    }

    private fun getLabel(
        dateRange: DateRange,
        dateRangeType: StatisticDateRangeType,
    ): String {
        return when (dateRangeType) {
            StatisticDateRangeType.WEEK -> dateTimeFormatter.formatDayOfWeek(
                date = dateRange.start,
                abbreviated = true,
            )
            StatisticDateRangeType.MONTH -> dateTimeFormatter.formatWeek(
                date = dateRange.start,
            )
            StatisticDateRangeType.QUARTER -> dateTimeFormatter.formatMonth(
                month = dateRange.start.month,
                abbreviated = false,
            )
            StatisticDateRangeType.YEAR -> dateTimeFormatter.formatMonth(
                month = dateRange.start.month,
                abbreviated = true,
            )
        }
    }
}