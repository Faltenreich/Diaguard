package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Rect

data class TimelineCanvasDimensions(
    val canvas: Rect,
    val statusBar: Rect,
    val chart: Rect,
    val table: Rect,
    val time: Rect,
    val scroll: Float,
)