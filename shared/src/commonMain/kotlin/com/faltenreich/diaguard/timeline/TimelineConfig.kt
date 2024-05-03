package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.faltenreich.diaguard.datetime.DayOfWeek
import com.faltenreich.diaguard.datetime.factory.DateTimeConstants

data class TimelineConfig(
    val daysOfWeek: Map<DayOfWeek, String>,

    val padding: Float,
    val fontPaint: Paint,
    val fontSize: Float,

    val backgroundColor: Color,
    val cornerRadius: CornerRadius = CornerRadius(x = 20f, y = 20f),

    val gridStrokeColor: Color,
    val gridStrokeWidth: Float = 0f,
    val gridShadowColor: Color,

    val valueColorNormal: Color,
    val valueColorLow: Color,
    val valueColorHigh: Color,
    val valueStrokeWidth: Float = 8f,
    val valueDotRadius: Float = 16f,

    private val xMin: Int = 0,
    private val xMax: Int = DateTimeConstants.HOURS_PER_DAY,
    val xStep: Int = 2,

    // TODO: Pass values
    val yMin: Int = 0,
    val yLow: Int = 70,
    val yHigh: Int = 180,
    val yMax: Int = 250,
    private val yStep: Int = 50,
) {

    private val xRange: IntRange = xMin .. xMax
    val xAxis: IntProgression = xRange step xStep
    val xAxisLabelCount: Int = xRange.last / xAxis.step

    private val yRange: IntRange = yMin .. yMax
    val yAxis: IntProgression = yRange step yStep

    val yLowFraction: Float = (yLow - yMin).toFloat() / (yMax - yMin).toFloat()
    val yHighFraction: Float = (yHigh - yMin).toFloat() / (yMax - yMin).toFloat()
    val valueStroke: Stroke = Stroke(width = valueStrokeWidth)
    val valuePath: Path = Path()
}