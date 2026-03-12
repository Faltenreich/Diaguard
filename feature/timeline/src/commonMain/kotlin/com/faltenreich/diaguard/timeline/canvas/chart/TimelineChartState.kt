package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue

data class TimelineChartState(
    val chartRectangle: Rect,
    val iconRectangle: Rect,
    val property: MeasurementProperty.Local,
    val items: List<Item>,
    val colorStops: List<ColorStop>,
    val labels: List<Label>,
) {

    data class Label(
        val position: Offset,
        val text: String,
    )

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