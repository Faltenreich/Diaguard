package com.faltenreich.diaguard.timeline.list

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.TimelineViewState

@Suppress("FunctionName")
fun DrawScope.TimelineList(
    state: TimelineViewState,
    config: TimelineConfig,
) = with(config) {
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