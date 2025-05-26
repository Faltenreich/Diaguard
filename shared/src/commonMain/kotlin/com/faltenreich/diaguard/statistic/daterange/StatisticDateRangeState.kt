package com.faltenreich.diaguard.statistic.daterange

import com.faltenreich.diaguard.datetime.DateRange

data class StatisticDateRangeState(
    val type: StatisticDateRangeType,
    val dateRange: DateRange,
    val dateRangeLocalized: String,
    val title: String,
)