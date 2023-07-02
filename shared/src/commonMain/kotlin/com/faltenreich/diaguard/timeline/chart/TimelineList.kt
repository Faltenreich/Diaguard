package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.shared.view.drawText

@Suppress("FunctionName")
fun DrawScope.TimelineList(
    state: TimelineChartState,
) = with(state) {
    state.propertiesForList.forEachIndexed { index, property ->
        val iconSize = fontSize
        val heightPerProperty = iconSize + padding * 2
        val x = state.listOrigin.x
        val y = state.listOrigin.y + index * heightPerProperty
        drawText(
            text = property.icon ?: "",
            x = x + padding,
            y = y + iconSize + padding,
            size = fontSize,
            paint = fontPaint,
        )
        drawLine(
            color = gridStrokeColor,
            start = Offset(x = state.listOrigin.x, y = y),
            end = Offset(x = state.listOrigin.x + state.listSize.width, y = y),
            strokeWidth = gridStrokeWidth,
        )
    }
}