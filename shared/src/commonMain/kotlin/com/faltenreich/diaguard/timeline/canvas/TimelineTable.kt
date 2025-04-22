package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.factory.DateTimeConstants
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.TimelineData

@Suppress("FunctionName")
fun DrawScope.TimelineTable(
    data: TimelineData.Table,
    initialDate: Date,
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) {
    val initialDateTime = initialDate.atStartOfDay()

    val x = coordinates.table.topLeft.x
    var y = coordinates.table.topLeft.y

    data.categories.forEachIndexed { categoryIndex, category ->
        val rowHeight = config.tableRowHeight

        category.properties.forEachIndexed { propertyIndex, property ->
            val isFirstRow = categoryIndex == 0 && propertyIndex == 0
            if (!isFirstRow) {
                // Divider
                drawLine(
                    color = config.gridStrokeColor,
                    start = Offset(x = coordinates.table.topLeft.x, y = y),
                    end = Offset(x = coordinates.table.topLeft.x + coordinates.table.size.width, y = y),
                    strokeWidth = config.gridStrokeWidth,
                )
            }

            val iconSize = rowHeight

            val labelSize = textMeasurer.measure(property.name)

            // Label background
            val path = Path()
            val rect = RoundRect(
                rect = Rect(
                    left = x + config.padding / 2,
                    top = y + config.fontSize - labelSize.size.height + config.padding / 2,
                    right = x + iconSize + labelSize.size.width + config.padding * 2,
                    bottom = y + config.fontSize + config.padding + config.padding / 2,
                ),
                cornerRadius = config.cornerRadius,
            )
            path.addRoundRect(rect)
            drawPath(
                path = path,
                color = config.backgroundColor,
            )

            // Label
            drawText(
                text = property.name,
                x = x + iconSize,
                y = y + config.fontSize + config.padding - 2, // TODO: Remove magic offset
                size = config.fontSize,
                paint = config.fontPaint,
            )

            MeasurementCategoryIcon(
                icon = category.icon,
                fallback = property.name,
                position = Offset(x, y),
                size = Size(width = iconSize, height = iconSize),
                textMeasurer = textMeasurer,
            )

            property.values.forEach { value ->
                val dateTime = value.dateTime

                val widthPerDay = coordinates.table.size.width
                val widthPerHour = widthPerDay / (config.xAxis.last / config.xAxis.step)
                val widthPerMinute = widthPerHour / DateTimeConstants.MINUTES_PER_HOUR

                val offsetInMinutes = initialDateTime.minutesUntil(dateTime)
                val offsetOfDateTime = (offsetInMinutes / config.xAxis.step) * widthPerMinute
                val offsetOfHour = coordinates.table.topLeft.x + coordinates.scroll.x + offsetOfDateTime

                val text = value.value
                val textSize = textMeasurer.measure(text)

                val valueX = offsetOfHour + widthPerHour / 2 - textSize.size.width / 2
                val valueY = y + rowHeight / 2 + config.fontSize / 2

                drawText(
                    text = text,
                    x = valueX,
                    y = valueY,
                    size = config.fontSize,
                    paint = config.fontPaint,
                )
            }
            y += rowHeight
        }
    }
}