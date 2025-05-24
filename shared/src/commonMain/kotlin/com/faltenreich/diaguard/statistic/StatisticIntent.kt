package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.measurement.category.MeasurementCategory

sealed interface StatisticIntent {

    data class SetCategory(val category: MeasurementCategory.Local) : StatisticIntent

    data class SetDateRange(val dateRange: DateRange) : StatisticIntent
}