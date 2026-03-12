package com.faltenreich.diaguard.timeline.canvas.table

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.food.eaten.FoodEaten
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.datetime.TimeUnit
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions
import com.faltenreich.diaguard.timeline.canvas.time.TimelineTimeState

class GetTimelineTableStateUseCase(
    private val mapValue: MeasurementValueMapper,
) {

    operator fun invoke(
        values: List<MeasurementValue.Local>,
        foodEaten: List<FoodEaten.Local>,
        properties: List<MeasurementProperty.Local>,
        decimalPlaces: Int,
        time: TimelineTimeState?,
        dimensions: TimelineCanvasDimensions.Calculated?,
    ): TimelineTableState? {
        if (time == null || dimensions == null) {
            return null
        }

        val categories = properties
            .mapNotNull(MeasurementProperty::category)
            .distinct()
            .sortedBy(MeasurementCategory::sortIndex)

        val rectangle = dimensions.table
        val rowSize = Size(
            width = rectangle.width,
            height = dimensions.table.height / properties.size,
        )
        var rowIndex = 0

        return TimelineTableState(
            rectangle = dimensions.table,
            categories = categories.map { category ->
                val propertiesOfCategory = properties.filter { it.category == category }

                TimelineTableState.Category(
                    category = category,
                    properties = propertiesOfCategory.map { property ->
                        val propertyRectangle = Rect(
                            offset = Offset(
                                x = rectangle.left,
                                y = rectangle.top + (rowIndex * rowSize.height),
                            ),
                            size = rowSize,
                        )
                        val iconRectangle = Rect(
                            offset = Offset(
                                x = propertyRectangle.left,
                                y = propertyRectangle.bottom - dimensions.tableRowHeight,
                            ),
                            size = Size(
                                width = dimensions.tableRowHeight,
                                height = dimensions.tableRowHeight,
                            ),
                        )

                        rowIndex++

                        val valuesForMeasurements = values
                            .filter { it.property == property }
                            .groupBy { it.entry.dateTime.normalized(time) }
                            .map { (dateTime, values) ->
                                val sum = values.sumOf { it.value }
                                val value = when (property.aggregationStyle) {
                                    MeasurementAggregationStyle.CUMULATIVE -> sum
                                    MeasurementAggregationStyle.AVERAGE -> sum / values.size
                                }
                                IntermediateValue(
                                    dateTime = dateTime,
                                    value = value,
                                    property = property,
                                )
                            }

                        val valuesForFoodEaten = foodEaten
                            .takeIf { property.key == DatabaseKey.MeasurementProperty.MEAL }
                            ?.groupBy { it.entry.dateTime.normalized(time) }
                            ?.map { (dateTime, foodEaten) ->
                                val carbohydrates = foodEaten.sumOf(FoodEaten::carbohydrates)
                                IntermediateValue(
                                    dateTime = dateTime,
                                    value = carbohydrates,
                                    property = property,
                                )
                            }
                            ?: emptyList()

                        val valuesMerged = (valuesForMeasurements + valuesForFoodEaten)
                            .groupBy { it.dateTime }
                            .map { (dateTime, values) ->
                                val sum = values.sumOf { it.value }
                                val value = when (property.aggregationStyle) {
                                    MeasurementAggregationStyle.CUMULATIVE -> sum
                                    MeasurementAggregationStyle.AVERAGE -> sum / values.size
                                }
                                IntermediateValue(
                                    dateTime = dateTime,
                                    value = value,
                                    property = property,
                                )
                            }

                        val valuesLocalized = valuesMerged.map { value ->
                            val widthPerDay = rectangle.size.width
                            val widthPerHour = widthPerDay /
                                (time.hourProgression.last / time.hourProgression.step)
                            val widthPerMinute =
                                widthPerHour / DateTimeConstants.MINUTES_PER_HOUR

                            val offsetInMinutes = time.initialDateTime.until(
                                value.dateTime,
                                TimeUnit.MINUTE,
                            ).inWholeMinutes
                            val offsetOfDateTime =
                                (offsetInMinutes / time.hourProgression.step) * widthPerMinute
                            val offsetOfHour =
                                propertyRectangle.left + dimensions.scroll + offsetOfDateTime
                            val valueRectangle = Rect(
                                offset = Offset(
                                    x = offsetOfHour,
                                    y = propertyRectangle.top,
                                ),
                                size = Size(
                                    width = widthPerHour,
                                    height = propertyRectangle.height,
                                )
                            )

                            TimelineTableState.Value(
                                rectangle = valueRectangle,
                                dateTime = value.dateTime,
                                value = mapValue(
                                    value = value.value,
                                    property = property,
                                    decimalPlaces = decimalPlaces,
                                ).value,
                                values = values,
                            )
                        }

                        TimelineTableState.Property(
                            property = property,
                            rectangle = propertyRectangle,
                            iconRectangle = iconRectangle,
                            name = property.name,
                            values = valuesLocalized,
                        )
                    }
                )
            },
        )
    }
}

private data class IntermediateValue(
    val dateTime: DateTime,
    val value: Double,
    val property: MeasurementProperty,
)

private fun DateTime.normalized(timeState: TimelineTimeState): DateTime {
    val hour = time.hourOfDay
    val hourNormalized = hour - (hour % timeState.hourProgression.step)
    return copy(
        hourOfDay = hourNormalized,
        minuteOfHour = 0,
        secondOfMinute = 0,
        millisOfSecond = 0,
        nanosOfMilli = 0,
    )
}