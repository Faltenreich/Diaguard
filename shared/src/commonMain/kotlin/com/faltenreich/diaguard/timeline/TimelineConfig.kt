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

    // TODO: Pass values
    val yMin: Double = 0.0,
    val yLow: Double?,
    val yHigh: Double?,
    val yMax: Double,
    val yStep: Double,
) {

    private val xRange: IntRange = xMin .. xMax
    val xAxis: IntProgression = xRange step xStep
    val xAxisLabelCount: Int = xRange.last / xAxis.step

    private val yRange: ClosedRange<Double> = yMin .. yMax
    val yAxis: Iterable<Double> = yRange step yStep

    val valueStroke: Stroke = Stroke(width = valueStrokeWidth)
    val valuePath: Path = Path()

    companion object {

        // TODO: Extract and make dynamic
        const val STEP = 2
    }
}

// TODO: Move and test or find other way
private infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
    require(start.isFinite())
    require(endInclusive.isFinite())
    require(step > 0.0) { "Step must be positive, was: $step." }
    val sequence = generateSequence(start) { previous ->
        if (previous == Double.POSITIVE_INFINITY) return@generateSequence null
        val next = previous + step
        if (next > endInclusive) null else next
    }
    return sequence.asIterable()
}