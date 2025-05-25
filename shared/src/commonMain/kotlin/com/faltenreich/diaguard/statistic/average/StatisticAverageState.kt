package com.faltenreich.diaguard.statistic.average

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class StatisticAverageState(
    val property: MeasurementProperty.Local,
    val value: String?,
    val countPerDay: String,
)