package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.statistic.average.StatisticAverageState
import com.faltenreich.diaguard.statistic.distribution.StatisticDistributionState
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState

data class StatisticState(
    val dateRange: DateRange,
    val dateRangeLocalized: String,
    val categories: List<MeasurementCategory.Local>,
    val category: MeasurementCategory.Local,
    val properties: List<MeasurementProperty.Local>,
    val property: MeasurementProperty.Local,
    val average: StatisticAverageState,
    val trend: StatisticTrendState,
    val distribution: StatisticDistributionState,
)