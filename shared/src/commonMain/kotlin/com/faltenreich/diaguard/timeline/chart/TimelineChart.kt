package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.timeline.chart.drawing.TimelineValues
import com.faltenreich.diaguard.timeline.chart.drawing.TimelineXAxis
import com.faltenreich.diaguard.timeline.chart.drawing.TimelineYAxis

@Suppress("FunctionName")
fun DrawScope.TimelineChart(
    state: TimelineChartState,
    config: TimelineChartConfig,
) {
    TimelineYAxis(config)
    TimelineXAxis(state, config)
    TimelineValues(state, config)
}