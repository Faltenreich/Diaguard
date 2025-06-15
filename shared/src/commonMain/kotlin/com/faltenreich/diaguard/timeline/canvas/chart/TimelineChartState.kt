package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

data class TimelineChartState(
    val rectangle: Rect,
    val values: List<Offset>,
    val valueStep: Double,
    val valueAxis: IntProgression,
    val colorStops: List<ColorStop>,
) {

    data class ColorStop(
        val offset: Float,
        val type: Type,
    ) {

        enum class Type {
            NONE,
            LOW,
            NORMAL,
            HIGH,
        }
    }
}