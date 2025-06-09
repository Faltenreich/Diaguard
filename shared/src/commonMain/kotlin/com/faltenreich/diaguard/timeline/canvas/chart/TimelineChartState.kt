package com.faltenreich.diaguard.timeline.canvas.chart

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class TimelineChartState(
    val initialDateTime: DateTime,
    val property: MeasurementProperty,
    val values: List<Value>,
    val valueMin: Double,
    val valueLow: Double?,
    val valueHigh: Double?,
    val valueMax: Double,
    val valueStep: Double,
) {

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