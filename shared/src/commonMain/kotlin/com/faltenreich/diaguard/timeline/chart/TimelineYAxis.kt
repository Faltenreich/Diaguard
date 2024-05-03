package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig

@Suppress("FunctionName")
fun DrawScope.TimelineYAxis(
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) {
    val heightPerSection = coordinates.chart.size.height / (config.yAxis.last / config.yAxis.step)
    config.yAxis
        .drop(0)
        .dropLast(1)
        .forEach { value ->
            val index = config.yAxis.indexOf(value)
            val x = coordinates.canvas.topLeft.x + config.padding
            val y = coordinates.canvas.topLeft.y + coordinates.chart.size.height - (index * heightPerSection)

            // Line
            drawLine(
                color = config.gridStrokeColor,
                start = Offset(x = coordinates.canvas.topLeft.x, y = y),
                end = Offset(x = coordinates.canvas.topLeft.x + coordinates.chart.size.width, y = y),
                strokeWidth = config.gridStrokeWidth,
            )

            val text = value.toString()
            val textSize = textMeasurer.measure(text)

            // Background
            val path = Path()
            val rect = RoundRect(
                rect = Rect(
                    left = x - config.padding / 2,
                    top = y - textSize.size.height - config.padding / 2,
                    right = x + textSize.size.width + config.padding,
                    bottom = y - config.padding / 2,
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
                text = text,
                x = x,
                y = y - config.padding,
                size = config.fontSize,
                paint = config.fontPaint,
            )
        }
}