package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class TimelineChartState(
    val values: List<Offset>,
    val colorStops: List<Pair<Float, Color>>,
)