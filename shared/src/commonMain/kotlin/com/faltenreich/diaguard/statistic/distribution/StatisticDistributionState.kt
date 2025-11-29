package com.faltenreich.diaguard.statistic.distribution

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValueTint

data class StatisticDistributionState(
    val property: MeasurementProperty.Local,
    val parts: List<Part>,
) {

    data class Part(
        val label: String,
        val percentage: Float,
        val tint: MeasurementValueTint,
    )
}
