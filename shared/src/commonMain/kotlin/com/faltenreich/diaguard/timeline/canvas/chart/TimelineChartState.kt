package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset

data class TimelineChartState(
    val values: List<Offset>,
    val colorStops: List<ColorStop>,
) {

    data class ColorStop(
        val offset: Float,
        val type: Type,
    ) {

        enum class Type {
            LOW,
            NORMAL,
            HIGH,
        }
    }
}