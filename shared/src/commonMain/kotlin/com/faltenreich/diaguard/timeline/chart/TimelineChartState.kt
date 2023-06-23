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

    private val xMin: Int = 0,
    private val xMax: Int = 24,
    private val xStep: Int = 2,

    private val yMin: Int = 0,
    private val yMax: Int = 250,
    private val yStep: Int = 50,
) {

    private val xRange: IntRange = xMin .. xMax
    val xAxis: IntProgression = xRange step xStep
    val xAxisLabelCount: Int = xRange.last / xAxis.step

    private val yRange: IntRange = yMin .. yMax
    val yAxis: IntProgression = yRange step yStep
}