package com.faltenreich.diaguard.dashboard.trend

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.statistic.trend.GetStatisticTrendUseCase
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest

class GetDashboardTrendUseCase(
    private val categoryRepository: MeasurementCategoryRepository,
    private val getStatisticTrend: GetStatisticTrendUseCase,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(): Flow<StatisticTrendState> {
        val categoryKey = DatabaseKey.MeasurementCategory.BLOOD_SUGAR
        val today = dateTimeFactory.today()
        return categoryRepository.observeByKey(categoryKey).flatMapLatest { category ->
            if (category != null) {
                getStatisticTrend(
                    category = category,
                    dateRange = today.minus(1, DateUnit.WEEK) .. today,
                )
            } else {
                emptyFlow()
            }
        }
    }
}