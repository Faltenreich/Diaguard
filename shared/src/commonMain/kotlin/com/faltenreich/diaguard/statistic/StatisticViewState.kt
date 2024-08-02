package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class StatisticViewState(
    val categories: List<MeasurementCategory.Local>,
    val selectedCategory: MeasurementCategory.Local,
    val dateRange: ClosedRange<Date>,
    val dateRangeLocalized: String,
    val average: Average,
) {

    data class Average(
        val values: List<Pair<MeasurementProperty, String?>>,
        val countPerDay: String,
    )
}