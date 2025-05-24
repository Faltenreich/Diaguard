package com.faltenreich.diaguard.statistic.trend

import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint

data class StatisticTrendState(
    val days: List<Day>,
    val targetValue: Double,
    val maximumValue: Double,
) {

    data class Day(
        val date: String,
        val average: Value?,
    )

    data class Value(
        val value: Double,
        val tint: MeasurementValueTint,
    )
}