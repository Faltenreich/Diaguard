package com.faltenreich.diaguard.statistic.trend

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.property.range.MeasurementValueRange
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.tint.GetMeasurementValueTintUseCase
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlin.math.max

class GetStatisticTrendUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val getValueTint: GetMeasurementValueTintUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(
        category: MeasurementCategory.Local,
        dateRange: ClosedRange<Date>,
    ): Flow<StatisticTrendState> {
        val propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR

        return propertyRepository.observeByKey(propertyKey).flatMapLatest { property ->
            combine(
                DateProgression(dateRange.start, dateRange.endInclusive).map { date ->
                    valueRepository.observeAverageByPropertyKey(
                        propertyKey = propertyKey,
                        minDateTime = date.atStartOfDay(),
                        maxDateTime = date.atEndOfDay(),
                    ).map { date to it }
                }
            ) { averagesByDate ->
                averagesByDate.map { (date, average) ->
                    StatisticTrendState.Day(
                        date = dateTimeFormatter.formatDayOfWeek(date, abbreviated = true),
                        average = if (average != null && property != null) {
                            val value = MeasurementValue.Average(
                                value = average,
                                property = property,
                            )
                            StatisticTrendState.Value(
                                value = average,
                                tint = getValueTint(value),
                            )
                        } else {
                            null
                        },
                    )
                }
            }.map { days ->
                val targetValue = property?.range?.target ?: MeasurementValueRange.BLOOD_SUGAR_TARGET_DEFAULT
                val maximumValue = days.mapNotNull { it.average?.value }.maxOrNull()
                val maximumValueDefault = targetValue * 2
                StatisticTrendState(
                    days = days,
                    targetValue = targetValue,
                    maximumValue = max(maximumValue ?: maximumValueDefault, maximumValueDefault),
                )
            }
        }
    }
}