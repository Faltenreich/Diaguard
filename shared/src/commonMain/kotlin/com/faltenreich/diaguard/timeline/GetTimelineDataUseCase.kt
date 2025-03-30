package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper

class GetTimelineDataUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val mapValue: MeasurementValueMapper,
) {

    operator fun invoke(
        categories: List<MeasurementCategory.Local>,
        values: List<MeasurementValue.Local>,
        decimalPlaces: Int,
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
        val properties = propertyRepository.getAll()
        return TimelineData(
            chart = TimelineData.Chart(valuesForChart),
            table = TimelineData.Table(
                categories = categories
                    .filterNot { it.isBloodSugar }
                    .map { category ->
                    val propertiesOfCategory = properties.filter { it.category == category }
                    TimelineData.Table.Category(
                        icon = category.icon,
                        name = category.name,
                        properties = propertiesOfCategory.map { property ->
                            TimelineData.Table.Category.Property(
                                name = property.name,
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
                                            // TODO: Decimal places may take up too much space
                                            value = mapValue(
                                                value = value,
                                                unit = property.selectedUnit,
                                                decimalPlaces = decimalPlaces,
                                            ).value,
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