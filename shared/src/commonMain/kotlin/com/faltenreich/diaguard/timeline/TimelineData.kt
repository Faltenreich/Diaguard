package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.DateTime

data class TimelineData(
    val chart: Chart,
    val table: Table,
) {

    data class Chart(
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

    data class Table(
        val categories: List<Category>,
    ) {

        val rowCount: Int = categories.sumOf { it.properties.size }

        data class Category(
            val icon: String?,
            val name: String,
            val properties: List<Property>,
        ) {

            data class Property(
                val name: String,
                val values: List<Value>,
            )

            data class Value(
                val dateTime: DateTime,
                val value: String,
            )
        }
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