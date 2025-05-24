package com.faltenreich.diaguard.statistic.distribution

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint
import com.faltenreich.diaguard.statistic.StatisticState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetDistributionUseCase {

    operator fun invoke(
        category: MeasurementCategory.Local,
        dateRange: ClosedRange<Date>,
    ): Flow<StatisticState.Distribution> {
        // TODO
        val targetCount = 100f
        val lowCount = 22f
        val highCount = 45f
        val totalCount = targetCount + lowCount + highCount
        return flowOf(
            StatisticState.Distribution(
                parts = listOf(
                    StatisticState.Distribution.Part(
                        percentage = targetCount / totalCount,
                        tint = MeasurementValueTint.NORMAL,
                    ),
                    StatisticState.Distribution.Part(
                        percentage = lowCount / totalCount,
                        tint = MeasurementValueTint.LOW,
                    ),
                    StatisticState.Distribution.Part(
                        percentage = highCount / totalCount,
                        tint = MeasurementValueTint.HIGH,
                    ),
                )
            )
        )
    }
}