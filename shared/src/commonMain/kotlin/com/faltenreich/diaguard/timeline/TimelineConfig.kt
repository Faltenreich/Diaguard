package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.datetime.DayOfWeek

data class TimelineConfig(
    val daysOfWeek: Map<DayOfWeek, String>,

    val padding: Float,
    val fontPaint: Paint,
    val fontSize: Float,
    val tableRowHeight: Float,

    val backgroundColor: Color,
    val cornerRadius: CornerRadius = CornerRadius(x = 20f, y = 20f),

    val gridStrokeColor: Color,
    val gridStrokeWidth: Float = 1f,
    val gridShadowColor: Color,

    val valueColorNormal: Color,
    val valueColorLow: Color,
    val valueColorHigh: Color,
    val valueStrokeWidth: Float = 8f,
    val valueDotRadius: Float = 16f,

    private val xMin: Int = 0,
    private val xMax: Int = DateTimeConstants.HOURS_PER_DAY,
    val xStep: Int = STEP,
) {

    private val xRange: IntRange = xMin .. xMax
    val xAxis: IntProgression = xRange step xStep
    val xAxisLabelCount: Int = xRange.last / xAxis.step

    val valueStroke: Stroke = Stroke(width = valueStrokeWidth)
    val valuePath: Path = Path()

    companion object {

        // TODO: Extract and make dynamic
        const val STEP = 2
    }
}