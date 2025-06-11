package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.canvas.TimelineCoordinates

@Suppress("FunctionName")
fun DrawScope.TimelineChart(
    state: TimelineChartState,
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) {
    TimelineChartValues(
        state = state,
        config = config,
    )
    TimelineChartLabels(
        state = state,
        coordinates = coordinates,
        config = config,
        textMeasurer = textMeasurer,
    )
}