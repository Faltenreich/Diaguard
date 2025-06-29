package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.timeline.TimelineConfig

@Suppress("FunctionName")
fun DrawScope.TimelineChart(
    state: TimelineChartState,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) {
    TimelineChartLabels(
        state = state,
        config = config,
        textMeasurer = textMeasurer,
    )
    TimelineChartValues(
        state = state,
        config = config,
    )
}