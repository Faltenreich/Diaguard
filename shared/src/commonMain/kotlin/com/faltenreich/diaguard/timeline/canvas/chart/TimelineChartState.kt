package com.faltenreich.diaguard.timeline.canvas.chart

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import kotlin.math.max

private const val Y_AXIS_MIN = 0.0
private const val Y_AXIS_STEP = 50.0
private const val Y_AXIS_MAX_MIN = 250.0

data class TimelineChartState(
    val initialDateTime: DateTime,
    val property: MeasurementProperty,
    val values: List<Value>,
) {

    val valueMin = Y_AXIS_MIN
    val valueLow = property.range.low
    val valueHigh = property.range.high
    val valueMax = max(
        Y_AXIS_MAX_MIN,
        (values.maxOfOrNull { it.value } ?: 0.0) + Y_AXIS_STEP,
    )
    val valueStep = Y_AXIS_STEP
    val axis = valueMin .. valueMax step valueStep

    data class Value(
        val dateTime: DateTime,
        val value: Double,
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