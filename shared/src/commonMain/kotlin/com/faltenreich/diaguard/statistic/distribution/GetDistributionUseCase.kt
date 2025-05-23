package com.faltenreich.diaguard.statistic.distribution

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.statistic.StatisticState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetDistributionUseCase {

    operator fun invoke(
        category: MeasurementCategory.Local,
        dateRange: ClosedRange<Date>,
    ): Flow<StatisticState.Distribution> {
        // TODO
        return flowOf(
            StatisticState.Distribution(
                targetCount = 100,
                lowCount = 22,
                highCount = 45,
            )
        )
    }
}