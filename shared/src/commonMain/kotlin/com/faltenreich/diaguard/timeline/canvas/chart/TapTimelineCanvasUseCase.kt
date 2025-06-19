package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasState

class TapTimelineCanvasUseCase {

    operator fun invoke(
        position: Offset,
        touchAreaSize: Size,
        canvas: TimelineCanvasState,
    ): TapTimelineChartResult {
        val touchArea = Rect(
            offset = Offset(
                x = position.x - touchAreaSize.width / 2,
                y = position.y - touchAreaSize.height / 2,
            ),
            size = touchAreaSize,
        )
        return if (canvas.chart.rectangle.contains(position)) {
            when (val item = canvas.chart.items.firstOrNull { touchArea.contains(it.position) }) {
                null -> TapTimelineChartResult.None
                else -> TapTimelineChartResult.Chart(item.value.entry)
            }
        } else if (touchArea.overlaps(canvas.table.rectangle)) {
            // TODO
            TapTimelineChartResult.None
        } else {
            TapTimelineChartResult.None
        }
    }
}