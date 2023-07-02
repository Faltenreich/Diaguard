package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
        drawText(
            text = property.icon ?: "",
            x = x + padding,
            y = y + iconSize + padding,
            size = fontSize,
            paint = fontPaint,
        )
        drawLine(
            color = gridStrokeColor,
            start = Offset(x = origin.x, y = y),
            end = Offset(x = origin.x + size.width, y = y),
            strokeWidth = gridStrokeWidth,
        )
    }
}