package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions
import com.faltenreich.diaguard.timeline.canvas.time.TimelineTimeState
import kotlin.math.max

class GetTimelineChartStateUseCase {

    operator fun invoke(
        values: List<MeasurementValue.Local>,
        property: MeasurementProperty.Local,
        time: TimelineTimeState?,
        dimensions: TimelineCanvasDimensions?,
    ): TimelineChartState? {
        if (time == null || dimensions == null) {
            return null
        }

        val rectangle = dimensions.chart
        val stepCount = time.hourProgression.last / time.hourProgression.step

        val valuesWithXCoordinate = values.map { value ->
            val dateTime = value.entry.dateTime
            val xAxisStep = DateTimeConstants.HOURS_PER_DAY / stepCount
            val widthPerDay = dimensions.chart.size.width
            val widthPerHour = widthPerDay / stepCount
            val widthPerMinute = widthPerHour / DateTimeConstants.MINUTES_PER_HOUR
            val offsetInMinutes = time.initialDateTime.minutesUntil(dateTime)
            val offsetOfDateTime = (offsetInMinutes / xAxisStep) * widthPerMinute
            value to dimensions.chart.topLeft.x + dimensions.scroll + offsetOfDateTime
        }

        val valueStep = Y_AXIS_STEP
        val valueMin = Y_AXIS_MIN
        // FIXME: Visible value should have precedence
        val valueMaxValueWithX = valuesWithXCoordinate.maxByOrNull { (value, _) -> value.value }
        val valueMaxValue = valueMaxValueWithX?.first?.value ?: 0.0
        val valueMaxX = valueMaxValueWithX?.second
        val valueMaxDistance =
            if (valueMaxX == null) 0f
            else if (valueMaxX < rectangle.left) rectangle.left - valueMaxX
            else if (valueMaxX > rectangle.right) valueMaxX - rectangle.right
            else 0f
        // TODO: Find right distance to remove from valueMaxValue
        val valueMax = max(Y_AXIS_MAX_MIN, valueMaxValue - valueMaxDistance)
        val valueMaxPadded = valueMax + valueStep
        val valueAxis = valueMin .. valueMaxPadded step valueStep

        val coordinates = valuesWithXCoordinate.map { (value, x) ->
            val percentage = (value.value - valueAxis.first()) / (valueAxis.last() - valueAxis.first())
            val y = dimensions.chart.topLeft.y +
                dimensions.chart.size.height -
                (percentage.toFloat() * dimensions.chart.size.height)
            Offset(x, y)
        }

        val colorStops = mutableListOf<TimelineChartState.ColorStop>()
        val valueLow = property.range.low?.takeIf { property.range.isHighlighted }
        val valueHigh = property.range.high?.takeIf { property.range.isHighlighted }
        valueHigh?.let {
            val yHighFraction = (valueHigh - valueMin).toFloat() / (valueMax - valueMin).toFloat()
            colorStops.add(TimelineChartState.ColorStop(1 - yHighFraction, TimelineChartState.ColorStop.Type.HIGH))
            colorStops.add(TimelineChartState.ColorStop(1 - yHighFraction, TimelineChartState.ColorStop.Type.NORMAL))
        }
        valueLow?.let {
            val yLowFraction = (valueLow - valueMin).toFloat() / (valueMax - valueMin).toFloat()
            colorStops.add(TimelineChartState.ColorStop(1 - yLowFraction, TimelineChartState.ColorStop.Type.NORMAL))
            colorStops.add(TimelineChartState.ColorStop(1 - yLowFraction, TimelineChartState.ColorStop.Type.LOW))
        }
        if (colorStops.isEmpty()) {
            colorStops.add(TimelineChartState.ColorStop(0f, TimelineChartState.ColorStop.Type.NONE))
            colorStops.add(TimelineChartState.ColorStop(1f, TimelineChartState.ColorStop.Type.NONE))
        }

        return TimelineChartState(
            rectangle = rectangle,
            values = coordinates,
            valueStep = valueStep,
            valueAxis = valueAxis,
            colorStops = colorStops,
        )
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

    companion object {

        private const val Y_AXIS_MIN = 0.0
        private const val Y_AXIS_STEP = 50.0
        private const val Y_AXIS_MAX_MIN = 200.0
    }
}