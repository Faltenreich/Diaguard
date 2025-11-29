package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.statistic.average.StatisticAverageState
import com.faltenreich.diaguard.statistic.category.StatisticCategoryState
import com.faltenreich.diaguard.statistic.daterange.StatisticDateRangeState
import com.faltenreich.diaguard.statistic.distribution.StatisticDistributionState
import com.faltenreich.diaguard.statistic.property.StatisticPropertyState
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState

data class StatisticState(
    val dateRange: StatisticDateRangeState,
    val category: StatisticCategoryState?,
    val property: StatisticPropertyState?,
    val average: StatisticAverageState?,
    val trend: StatisticTrendState?,
    val distribution: StatisticDistributionState?,
) {

    constructor(dateRange: StatisticDateRangeState) : this(
        dateRange = dateRange,
        category = null,
        property = null,
        average = null,
        trend = null,
        distribution = null,
    )
}