package com.faltenreich.diaguard.dashboard.today

import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDashboardTodayUseCase(
    private val repository: MeasurementValueRepository,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(): Flow<DashboardTodayState> {
        val today = dateTimeFactory.today()
        return repository.observeByCategory(
            categoryKey = DatabaseKey.MeasurementCategory.BLOOD_SUGAR,
            minDateTime = today.atStartOfDay(),
            maxDateTime = today.atEndOfDay(),
        ).map { values ->
            DashboardTodayState(
                totalCount = values.size,
                hypoCount = values.count(MeasurementValue::isTooLow),
                hyperCount = values.count(MeasurementValue::isTooHigh),
            )
        }
    }
}