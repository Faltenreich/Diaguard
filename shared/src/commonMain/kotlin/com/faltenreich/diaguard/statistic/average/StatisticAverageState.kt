package com.faltenreich.diaguard.statistic.average

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class StatisticAverageState(
    val values: List<Pair<MeasurementProperty, String?>>,
    val countPerDay: String,
)