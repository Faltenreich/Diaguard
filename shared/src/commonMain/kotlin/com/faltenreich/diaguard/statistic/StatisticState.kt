package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.statistic.average.StatisticAverageState
import com.faltenreich.diaguard.statistic.distribution.StatisticDistributionState
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState

data class StatisticState(
    val category: MeasurementCategory.Local,
    val dateRange: DateRange,
    val dateRangeLocalized: String,
    val categories: List<MeasurementCategory.Local>,
    val average: StatisticAverageState,
    val trend: StatisticTrendState,
    val distribution: StatisticDistributionState,
)