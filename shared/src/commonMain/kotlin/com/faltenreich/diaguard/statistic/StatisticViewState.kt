package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.datetime.Date

sealed interface StatisticViewState {

    data class Loaded(
        val categories: List<MeasurementCategory>,
        val selectedCategory: MeasurementCategory,
        val dateRange: ClosedRange<Date>,
        val dateRangeLocalized: String,
        val average: Average,
    ) : StatisticViewState {

        data class Average(
            val values: List<Pair<MeasurementProperty, String?>>,
            val countPerDay: String,
        )
    }
}