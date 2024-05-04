package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTimelineDataUseCase(
    private val categoryRepository: MeasurementCategoryRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val numberFormatter: NumberFormatter,
) {

    operator fun invoke(date: Date): Flow<TimelineData> {
        return valueRepository.observeByDateRange(
            startDateTime = date.minus(2, DateUnit.DAY).atStartOfDay(),
            endDateTime = date.plus(2, DateUnit.DAY).atEndOfDay(),
        ).map { values ->
            val valuesForChart = values
                .filter { value -> value.property.category.isBloodSugar }
                .map { value ->
                    TimelineData.Chart.Value(
                        dateTime = value.entry.dateTime,
                        value = value.value, // TODO: Map to MeasurementValueForUser?
                    )
                }
            val valuesForTable = values.filterNot { value -> value.property.category.isBloodSugar }
            // TODO: Filter by user selection
            val categories = categoryRepository.getAll()
                .filterNot(MeasurementCategory::isBloodSugar)
                .onEach { category ->
                    // TODO: Improve performance
                    category.properties = propertyRepository.getByCategoryId(category.id)
                }
            TimelineData(
                chart = TimelineData.Chart(valuesForChart),
                table = TimelineData.Table(
                    categories = categories.map { category ->
                        TimelineData.Table.Category(
                            label = category.icon ?: category.name,
                            values = listOf(),
                            properties = category.properties.map { property ->
                                TimelineData.Table.Property(
                                    label = property.name,
                                    values = valuesForTable
                                        .filter { it.property == property }
                                        .groupBy { value ->
                                            val hour = value.entry.dateTime.time.hourOfDay
                                            val hourNormalized = hour - (hour % TimelineConfig.STEP)
                                            value.entry.dateTime.copy(
                                                hourOfDay = hourNormalized,
                                                minuteOfHour = 0,
                                                secondOfMinute = 0,
                                                millisOfSecond = 0,
                                                nanosOfMilli = 0,
                                            )
                                        }
                                        .map { (dateTime, values) ->
                                            val sum = values.sumOf { it.value }
                                            val value = when (property.aggregationStyle) {
                                                MeasurementAggregationStyle.CUMULATIVE -> sum
                                                MeasurementAggregationStyle.AVERAGE -> sum / values.size
                                            }
                                            TimelineData.Table.Value(
                                                dateTime = dateTime,
                                                value = numberFormatter(value),
                                            )
                                        },
                                )
                            }
                        )
                    },
                )
            )
        }
    }
}