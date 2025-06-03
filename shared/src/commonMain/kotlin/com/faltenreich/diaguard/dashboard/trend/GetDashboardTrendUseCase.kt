package com.faltenreich.diaguard.dashboard.trend

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.statistic.daterange.StatisticDateRangeType
import com.faltenreich.diaguard.statistic.trend.GetStatisticTrendUseCase
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest

class GetDashboardTrendUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val getStatisticTrend: GetStatisticTrendUseCase,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(): Flow<StatisticTrendState> {
        val key = DatabaseKey.MeasurementProperty.BLOOD_SUGAR
        val today = dateTimeFactory.today()
        return propertyRepository.observeByKey(key).flatMapLatest { property ->
            if (property != null) {
                getStatisticTrend(
                    property = property,
                    dateRange = today
                        .minus(1, DateUnit.WEEK)
                        .plus(1, DateUnit.DAY) .. today,
                    dateRangeType = StatisticDateRangeType.WEEK,
                )
            } else {
                emptyFlow()
            }
        }
    }
}