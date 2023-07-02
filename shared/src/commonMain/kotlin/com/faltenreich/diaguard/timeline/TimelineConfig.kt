package com.faltenreich.diaguard.timeline

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter

data class TimelineConfig(
    val textMeasurer: TextMeasurer,
    val dateTimeFormatter: DateTimeFormatter,

    val padding: Float,
    val fontPaint: Paint,
    val fontSize: Float,

    val gridStrokeColor: Color,
    val gridStrokeWidth: Float = 0f,
    val gridShadowColor: Color,

    val valueColorNormal: Color,
    val valueColorLow: Color,
    val valueColorHigh: Color,
    val valueStrokeWidth: Float = 8f,
    val valueDotRadius: Float = 16f,

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