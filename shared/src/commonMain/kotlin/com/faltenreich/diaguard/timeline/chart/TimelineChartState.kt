package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import com.faltenreich.diaguard.measurement.value.MeasurementValue

data class TimelineChartState(
    val values: List<MeasurementValue>,
    val offset: Offset,

    val padding: Float,
    val paint: Paint,
    val fontSize: Float,
    val strokeWidth: Float = 4f,

    val xMin: Int = 0,
    val xMax: Int = 24,
    val xStep: Int = 2,

    val yMin: Int = 0,
    val yMax: Int = 250,
    val yStep: Int = 50,
)