package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.primitive.NumberFormatter

class GetTimelineDataUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val numberFormatter: NumberFormatter,
) {

    operator fun invoke(
        categories: List<MeasurementCategory>,
        values: List<MeasurementValue>,
    ): TimelineData {
        val valuesForChart = values
            .filter { value -> value.property.category.isBloodSugar }
            .map { value ->
                TimelineData.Chart.Value(
                    dateTime = value.entry.dateTime,
                    value = value.value, // TODO: Map to MeasurementValueForUser?
                )
            }
        val valuesForTable = values.filterNot { value -> value.property.category.isBloodSugar }
        categories.onEach { category ->
            // TODO: Improve performance
            category.properties = propertyRepository.getByCategoryId(category.id)
        }
        return TimelineData(
            chart = TimelineData.Chart(valuesForChart),
            table = TimelineData.Table(
                categories = categories.map { category ->
                    TimelineData.Table.Category(
                        properties = category.properties.map { property ->
                            TimelineData.Table.Category.Property(
                                icon = category.icon,
                                name = category.name,
                                unit = property.name.takeIf { category.properties.size > 1 },
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
                                        TimelineData.Table.Category.Value(
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