package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig

@Suppress("FunctionName")
fun DrawScope.TimelineYAxis(
    origin: Offset,
    size: Size,
    config: TimelineConfig,
) = with(config) {
    val heightPerSection = size.height / (yAxis.last / yAxis.step)
    yAxis
        .drop(0)
        .dropLast(1)
        .forEach { value ->
            val index = yAxis.indexOf(value)
            val x = origin.x + padding
            val y = origin.y + size.height - (index * heightPerSection)

            drawLine(
                color = gridStrokeColor,
                start = Offset(x = origin.x, y = y),
                end = Offset(x = origin.x + size.width, y = y),
                strokeWidth = gridStrokeWidth,
            )

            val text = value.toString()
            val textSize = textMeasurer.measure(text)

            val path = Path()
            val rect = RoundRect(
                rect = Rect(
                    left = x - padding / 2,
                    top = y - textSize.size.height - padding / 2,
                    right = x + textSize.size.width + padding,
                    bottom = y - padding / 2,
                ),
                cornerRadius = cornerRadius,
            )
            path.addRoundRect(rect)
            drawPath(
                path = path,
                color = backgroundColor,
            )

            drawText(
                text = text,
                x = x,
                y = y - padding,
                size = fontSize,
                paint = fontPaint,
            )
        }
}