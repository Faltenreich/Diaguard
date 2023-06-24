package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter

data class TimelineChartState(
    val values: List<MeasurementValue>,
    val initialDate: Date,
    val offset: Offset,

    val dateTimeFormatter: DateTimeFormatter,

    val padding: Float,
    val paint: Paint,
    val fontSize: Float,
    val strokeWidth: Float = 12f,

    val lineColorNormal: Color,
    val lineColorLow: Color = Color.Blue,
    val lineColorHigh: Color = Color.Red,

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