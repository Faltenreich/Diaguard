package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.TimelineState

@Suppress("FunctionName")
fun DrawScope.TimelineChart(
    state: TimelineState,
    config: TimelineConfig,
) {
    TimelineYAxis(config)
    TimelineXAxis(state, config)
    TimelineValues(state, config)
}