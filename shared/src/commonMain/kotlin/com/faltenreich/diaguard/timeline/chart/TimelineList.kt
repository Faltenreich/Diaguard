package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig

@Suppress("FunctionName")
fun DrawScope.TimelineList(
    origin: Offset,
    size: Size,
    config: TimelineConfig,
    properties: List<MeasurementProperty>,
) = with(config) {
    properties.forEachIndexed { index, property ->
        val iconSize = fontSize
        val heightPerProperty = iconSize + padding * 2

        val x = origin.x
        val y = origin.y + index * heightPerProperty

        drawLine(
            color = gridStrokeColor,
            start = Offset(x = origin.x, y = y),
            end = Offset(x = origin.x + size.width, y = y),
            strokeWidth = gridStrokeWidth,
        )

        val text = property.icon ?: ""
        val textSize = textMeasurer.measure(text)

        val path = Path()
        val rect = RoundRect(
            rect = Rect(
                left = x + padding / 2,
                top = y + iconSize - textSize.size.height + padding / 2,
                right = x + textSize.size.width + padding * 2,
                bottom = y + iconSize + padding + padding / 2,
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
            x = x + padding,
            y = y + iconSize + padding / 1.5f,
            size = fontSize,
            paint = fontPaint,
        )
    }
}