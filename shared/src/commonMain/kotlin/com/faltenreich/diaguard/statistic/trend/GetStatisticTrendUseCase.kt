package com.faltenreich.diaguard.statistic.trend

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.range.MeasurementValueRange
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.tint.GetMeasurementValueTintUseCase
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
    ): Flow<StatisticTrendState> {
        return combine(
            DateProgression(dateRange.start, dateRange.endInclusive).map { date ->
                observeAverage(property, date).map { date to it }
            }
        ) { averagesByDate ->
            averagesByDate.map { (date, average) ->
                StatisticTrendState.Day(
                    date = dateTimeFormatter.formatDayOfWeek(date, abbreviated = true),
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
                days = days,
                targetValue = targetValue,
                maximumValue = max(maximumValue ?: maximumValueDefault, maximumValueDefault),
            )
        }
    }

    private fun observeAverage(
        property: MeasurementProperty.Local,
        date: Date,
    ): Flow<Double?> {
        return valueRepository.observeAverageByPropertyId(
            propertyId = property.id,
            minDateTime = date.atStartOfDay(),
            maxDateTime = date.atEndOfDay(),
        )
    }
}