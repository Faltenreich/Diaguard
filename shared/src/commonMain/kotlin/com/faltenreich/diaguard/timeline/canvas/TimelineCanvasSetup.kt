package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Size

data class TimelineCanvasSetup(
    val canvasSize: Size,
    val tableRowHeight: Float,
    val statusBarHeight: Int,
)