package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

sealed interface TimelineCanvasDimensions {

    data class Positioned(
        val canvasSize: Size,
        val tableRowHeight: Float,
        val statusBarHeight: Int,
    ) : TimelineCanvasDimensions

    data class Calculated(
        val canvas: Rect,
        val statusBar: Rect,
        val chart: Rect,
        val table: Rect,
        val tableRowHeight: Float,
        val time: Rect,
        val scroll: Float,
    ) : TimelineCanvasDimensions
}