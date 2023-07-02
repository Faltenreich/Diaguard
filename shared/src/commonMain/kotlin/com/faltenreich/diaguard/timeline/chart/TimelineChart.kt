package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.graphics.drawscope.DrawScope

@Suppress("FunctionName")
fun DrawScope.TimelineChart(
    state: TimelineChartState,
) {
    TimelineYAxis(state)
    TimelineXAxis(state)
    TimelineValues(state)
}