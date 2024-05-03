package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig

@Suppress("FunctionName")
fun DrawScope.TimelineList(
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
    categories: List<MeasurementCategory>,
    values: List<MeasurementValue>,
    textMeasurer: TextMeasurer,
) {
    categories.forEachIndexed { index, category ->
        val iconSize = config.fontSize
        val heightPerCategory = iconSize + config.padding * 2

        val x = coordinates.list.topLeft.x
        val y = coordinates.list.topLeft.y + index * heightPerCategory

        if (index > 0) {
            // Divider
            drawLine(
                color = config.gridStrokeColor,
                start = Offset(x = coordinates.list.topLeft.x, y = y),
                end = Offset(x = coordinates.list.topLeft.x + coordinates.list.size.width, y = y),
                strokeWidth = config.gridStrokeWidth,
            )
        }

        val text = category.icon ?: ""
        val textSize = textMeasurer.measure(text)

        // Icon background
        val path = Path()
        val rect = RoundRect(
            rect = Rect(
                left = x + config.padding / 2,
                top = y + iconSize - textSize.size.height + config.padding / 2,
                right = x + textSize.size.width + config.padding * 2,
                bottom = y + iconSize + config.padding + config.padding / 2,
            ),
            cornerRadius = config.cornerRadius,
        )
        path.addRoundRect(rect)
        drawPath(
            path = path,
            color = config.backgroundColor,
        )

        // Icon
        drawText(
            text = text,
            x = x + config.padding,
            y = y + iconSize + config.padding / 1.5f,
            size = config.fontSize,
            paint = config.fontPaint,
        )

        // TODO: Calculate sum or average in time range in ViewModel
        val valuesOfCategory = values.filter { it.property.category == category }
        valuesOfCategory.firstOrNull()?.let { value ->
            val hour = value.entry.dateTime.time.hourOfDay
            val hourPerSteps = hour / config.xStep
            val widthPerDay = coordinates.canvas.size.width
            val widthPerHour = (widthPerDay / config.xAxisLabelCount).toInt()
            val valueX = x + config.padding + widthPerHour * hourPerSteps
            val valueY = y + heightPerCategory / 2 + config.fontSize / 2
            drawText(
                text = value.value.toString(),
                x = valueX,
                y = valueY,
                size = config.fontSize,
                paint = config.fontPaint,
            )
        }
    }
}