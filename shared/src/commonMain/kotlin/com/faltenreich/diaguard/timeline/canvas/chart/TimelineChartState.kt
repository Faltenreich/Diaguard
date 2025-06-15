package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.faltenreich.diaguard.measurement.value.MeasurementValue

data class TimelineChartState(
    val rectangle: Rect,
    val items: List<Item>,
    val colorStops: List<ColorStop>,
    val valueStep: Double,
    val valueAxis: IntProgression,
) {

    data class Item(
        val value: MeasurementValue.Local,
        val position: Offset,
    )

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