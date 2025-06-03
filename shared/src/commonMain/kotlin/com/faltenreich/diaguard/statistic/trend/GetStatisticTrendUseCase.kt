package com.faltenreich.diaguard.statistic.trend

import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.DateRangeProgression
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.range.MeasurementValueRange
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.tint.GetMeasurementValueTintUseCase
import com.faltenreich.diaguard.statistic.daterange.StatisticDateRangeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.math.max

class GetStatisticTrendUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val getValueTint: GetMeasurementValueTintUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(
        property: MeasurementProperty.Local,
        dateRange: DateRange,
        dateRangeType: StatisticDateRangeType,
    ): Flow<StatisticTrendState> {
        return combine(
            DateRangeProgression(
                dateRange = dateRange,
                intervalDateUnit = dateRangeType.intervalDateUnit,
            ).map { intervalDateRange ->
                observeAverage(property, intervalDateRange).map { intervalDateRange to it }
            }
        ) { averagesByInterval ->
            averagesByInterval.map { (intervalDateRange, average) ->
                StatisticTrendState.Interval(
                    dateRange = intervalDateRange,
                    // TODO: Differentiate by StatisticDateRangeType
                    label = dateTimeFormatter.formatDayOfWeek(
                        date = intervalDateRange.start,
                        abbreviated = true,
                    ),
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

    private fun observeAverage(
        property: MeasurementProperty.Local,
        dateRange: DateRange,
    ): Flow<Double?> {
        return valueRepository.observeAverageByPropertyId(
            propertyId = property.id,
            minDateTime = dateRange.start.atStartOfDay(),
            maxDateTime = dateRange.endInclusive.atEndOfDay(),
        )
    }
}