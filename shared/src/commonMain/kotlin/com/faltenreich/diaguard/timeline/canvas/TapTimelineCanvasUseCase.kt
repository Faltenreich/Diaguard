package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

class TapTimelineCanvasUseCase {

    operator fun invoke(
        position: Offset,
        touchAreaSize: Size,
        canvas: TimelineCanvasState,
    ): TapTimelineCanvasResult {
        val touchArea = Rect(
            offset = Offset(
                x = position.x - touchAreaSize.width / 2,
                y = position.y - touchAreaSize.height / 2,
            ),
            size = touchAreaSize,
        )
        return if (canvas.chart.rectangle.contains(position)) {
            when (val item = canvas.chart.items.firstOrNull { touchArea.contains(it.position) }) {
                null -> TapTimelineCanvasResult.None
                else -> TapTimelineCanvasResult.Chart(item.value)
            }
        } else if (touchArea.overlaps(canvas.table.rectangle)) {
            // TODO
            TapTimelineCanvasResult.None
        } else {
            TapTimelineCanvasResult.None
        }
    }
}