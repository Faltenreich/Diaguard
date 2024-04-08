package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.measurement.category.MeasurementCategory

sealed interface StatisticIntent {

    data class Select(val category: MeasurementCategory) : StatisticIntent
}