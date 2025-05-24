package com.faltenreich.diaguard.statistic.distribution

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint

data class StatisticDistributionState(
    val properties: List<Property>,
) {

    data class Property(
        val property: MeasurementProperty.Local,
        val parts: List<Part>,
    )

    data class Part(
        val percentage: Float,
        val tint: MeasurementValueTint,
    )
}