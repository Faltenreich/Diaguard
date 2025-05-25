package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.statistic.average.StatisticAverageState
import com.faltenreich.diaguard.statistic.category.StatisticCategoryState
import com.faltenreich.diaguard.statistic.distribution.StatisticDistributionState
import com.faltenreich.diaguard.statistic.property.StatisticPropertyState
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState

data class StatisticState(
    val dateRange: DateRange,
    val dateRangeLocalized: String,
    val category: StatisticCategoryState?,
    val property: StatisticPropertyState?,
    val average: StatisticAverageState?,
    val trend: StatisticTrendState?,
    val distribution: StatisticDistributionState?,
) {

    constructor(
        dateRange: DateRange,
        dateRangeLocalized: String,
    ) : this(
        dateRange = dateRange,
        dateRangeLocalized = dateRangeLocalized,
        category = null,
        property = null,
        average = null,
        trend = null,
        distribution = null,
    )
}