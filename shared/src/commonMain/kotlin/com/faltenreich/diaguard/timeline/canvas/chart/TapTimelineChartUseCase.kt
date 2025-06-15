package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.timeline.TimelineIntent

class TapTimelineChartUseCase {

    operator fun invoke(intent: TimelineIntent.TapCanvas): TapTimelineChartResult = with(intent) {
        val canvas = state.canvas ?: return TapTimelineChartResult.None
        // TODO: Find sweet spot
        val touchAreaSize = Size(
            width = 50f,
            height = 50f,
        )
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