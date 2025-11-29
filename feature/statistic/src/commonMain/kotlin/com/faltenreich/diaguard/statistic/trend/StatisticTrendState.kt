package com.faltenreich.diaguard.statistic.trend

import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.measurement.value.MeasurementValueTint

data class StatisticTrendState(
    val intervals: List<Interval>,
    val targetValue: Double,
    val maximumValue: Double,
) {

    data class Interval(
        val dateRange: DateRange,
        val label: String,
        val average: Value?,
    )

    data class Value(
        val value: Double,
        val tint: MeasurementValueTint,
    )
}