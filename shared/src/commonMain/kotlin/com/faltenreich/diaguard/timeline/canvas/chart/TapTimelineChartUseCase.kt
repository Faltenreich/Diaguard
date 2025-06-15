package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.timeline.TimelineState

class TapTimelineChartUseCase {

    operator fun invoke(
        position: Offset,
        state: TimelineState,
    ): TapTimelineChartResult {
        val canvas = state.canvas ?: return TapTimelineChartResult.None
        // TODO: Extract
        val touchAreaSize = Size(20f, 20f)
        val touchArea = Rect(
            offset = Offset(
                x = position.x - touchAreaSize.width / 2,
                y = position.y - touchAreaSize.height / 2,
            ),
            size = touchAreaSize,
        )
        return if (touchArea.overlaps(canvas.chart.rectangle)) {
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