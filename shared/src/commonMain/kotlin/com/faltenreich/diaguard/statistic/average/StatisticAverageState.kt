package com.faltenreich.diaguard.statistic.average

import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty

data class StatisticAverageState(
    val property: MeasurementProperty.Local,
    val countPerDay: String,
    val value: String?,
)