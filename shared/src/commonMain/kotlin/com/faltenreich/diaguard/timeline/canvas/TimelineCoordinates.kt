package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.timeline.TimelineConfig

@Deprecated("Contents will move into states")
data class TimelineCoordinates(
    val canvas: Rect,
    val chart: Rect,
    val table: Rect,
    val time: Rect,
    val scroll: Offset,
    val valueMin: Double,
    val valueLow: Double?,
    val valueHigh: Double?,
    val valueMax: Double,
    val valueStep: Double,
    val valueAxis: Iterable<Double>,
) {

    companion object {

        private const val Y_AXIS_MIN = 0.0
        private const val Y_AXIS_STEP = 50.0
        private const val Y_AXIS_MAX_MIN = 250.0

        fun from(
            size: Size,
            scrollOffset: Offset,
            tableRowCount: Int,
            config: TimelineConfig,
        ): TimelineCoordinates {
            val origin = Offset.Zero

            val timeSize = Size(
                width = size.width,
                height = config.tableRowHeight,
            )
            val tableItemHeight = config.tableRowHeight
            val tableSize = Size(
                width = size.width,
                height = tableItemHeight * tableRowCount,
            )
            val chartSize = Size(
                width = size.width,
                height = size.height - tableSize.height - timeSize.height,
            )

            val tableOrigin = Offset(
                x = origin.x,
                y =  origin.y + chartSize.height,
            )
            val timeOrigin = Offset(
                x = origin.x,
                y = origin.y + chartSize.height + tableSize.height,
            )

            return TimelineCoordinates(
                canvas = Rect(offset = origin, size = size),
                chart = Rect(offset = origin, size = chartSize),
                table = Rect(offset = tableOrigin, size = tableSize),
                time = Rect(offset = timeOrigin, size = timeSize),
                scroll = scrollOffset,
                valueMin = 0.0,
                valueLow = 60.0,
                valueHigh = 180.0,
                valueMax = 250.0,
                valueStep = 50.0,
                valueAxis = 0.0 .. 250.0 step 50.0,
            )
        }
    }
}

// TODO: Move and test or find other way
infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
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