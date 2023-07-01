package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.TimelineViewState

@Suppress("FunctionName")
fun DrawScope.TimelineChart(
    state: TimelineViewState,
    config: TimelineConfig,
) {
    TimelineYAxis(config)
    TimelineXAxis(state, config)
    TimelineValues(state, config)
}