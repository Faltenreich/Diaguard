package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeConstants
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




        category.properties.forEachIndexed { propertyIndex, property ->
            val isFirstRow = categoryIndex == 0 && propertyIndex == 0
            if (!isFirstRow) {
                // Divider
                drawLine(
                    color = config.gridStrokeColor,
                    start = Offset(x = coordinates.table.topLeft.x, y = y),
                    end = Offset(
                        x = coordinates.table.topLeft.x + coordinates.table.size.width,
                        y = y
                    ),
                    strokeWidth = config.gridStrokeWidth,
                )
            }
            TimelineRow(
                x = x,
                y = y,
                label = property.label,
                values = property.values,
                initialDateTime = initialDateTime,
                coordinates = coordinates,
                config = config,
                textMeasurer = textMeasurer,
            )
            val rowHeight = config.fontSize + config.padding * 2
            y += rowHeight
        }
    }
}

@Suppress("FunctionName")
private fun DrawScope.TimelineRow(
    x: Float,
    y: Float,
    label: String,
    values: List<TimelineData.Table.Value>,
    initialDateTime: DateTime,
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) {
    val rowHeight = config.fontSize + config.padding * 2
    val labelSize = textMeasurer.measure(label)

    // Label background
    val path = Path()
    val rect = RoundRect(
        rect = Rect(
            left = x + config.padding / 2,
            top = y + config.fontSize - labelSize.size.height + config.padding / 2,
            right = x + labelSize.size.width + config.padding * 2,
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
        text = label,
        x = x + config.padding,
        y = y + config.fontSize + config.padding / 1.5f,
        size = config.fontSize,
        paint = config.fontPaint,
    )

    values.forEach { value ->
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
}