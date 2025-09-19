package com.faltenreich.diaguard.timeline.canvas.table

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.aggregationstyle.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions
import com.faltenreich.diaguard.timeline.canvas.time.TimelineTimeState

class GetTimelineTableStateUseCase(
    private val mapValue: MeasurementValueMapper,
) {

    operator fun invoke(
        values: List<MeasurementValue.Local>,
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

                        TimelineTableState.Property(
                            property = property,
                            rectangle = propertyRectangle,
                            iconRectangle = iconRectangle,
                            name = property.name,
                            values = values
                                .filter { it.property == property }
                                .groupBy { value ->
                                    val hour = value.entry.dateTime.time.hourOfDay
                                    val hourNormalized = hour - (hour % time.hourProgression.step)
                                    value.entry.dateTime.copy(
                                        hourOfDay = hourNormalized,
                                        minuteOfHour = 0,
                                        secondOfMinute = 0,
                                        millisOfSecond = 0,
                                        nanosOfMilli = 0,
                                    )
                                }
                                .map { (dateTime, values) ->
                                    val widthPerDay = rectangle.size.width
                                    val widthPerHour = widthPerDay /
                                        (time.hourProgression.last / time.hourProgression.step)
                                    val widthPerMinute =
                                        widthPerHour / DateTimeConstants.MINUTES_PER_HOUR

                                    val offsetInMinutes =
                                        time.initialDateTime.minutesUntil(dateTime)
                                    val offsetOfDateTime =
                                        (offsetInMinutes / time.hourProgression.step) *
                                            widthPerMinute
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

                                    val sum = values.sumOf { it.value }
                                    val value = when (property.aggregationStyle) {
                                        MeasurementAggregationStyle.CUMULATIVE -> sum
                                        MeasurementAggregationStyle.AVERAGE -> sum / values.size
                                    }

                                    TimelineTableState.Value(
                                        rectangle = valueRectangle,
                                        dateTime = dateTime,
                                        value = mapValue(
                                            value = value,
                                            property = property,
                                            decimalPlaces = decimalPlaces,
                                        ).value,
                                        values = values,
                                    )
                                },
                        )
                    }
                )
            },
        )
    }
}