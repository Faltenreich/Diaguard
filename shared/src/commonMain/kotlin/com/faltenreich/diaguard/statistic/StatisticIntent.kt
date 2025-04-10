package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.measurement.category.MeasurementCategory

sealed interface StatisticIntent {

    data class SetCategory(val category: MeasurementCategory.Local) : StatisticIntent
}