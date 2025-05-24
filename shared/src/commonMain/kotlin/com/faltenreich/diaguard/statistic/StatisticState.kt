package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint

data class StatisticState(
    val category: MeasurementCategory.Local,
    val dateRange: String,
    val categories: List<MeasurementCategory.Local>,
    val average: Average,
    val distribution: Distribution,
) {

    data class Average(
        val values: List<Pair<MeasurementProperty, String?>>,
        val countPerDay: String,
    )

    data class Distribution(
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
}