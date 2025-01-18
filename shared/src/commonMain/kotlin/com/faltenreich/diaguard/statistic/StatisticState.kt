package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class StatisticState(
    val category: MeasurementCategory.Local,
    val dateRange: String,
    val categories: List<MeasurementCategory.Local>,
    val average: Average,
) {

    data class Average(
        val values: List<Pair<MeasurementProperty, String?>>,
        val countPerDay: String,
    )
}