package com.faltenreich.diaguard.statistic.category

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory

data class StatisticCategoryState(
    val categories: List<MeasurementCategory.Local>,
    val selection: MeasurementCategory.Local,
)