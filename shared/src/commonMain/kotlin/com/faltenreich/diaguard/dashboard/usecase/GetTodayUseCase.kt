package com.faltenreich.diaguard.dashboard.usecase

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTodayUseCase(
    private val measurementCategoryRepository: MeasurementCategoryRepository = inject(),
    private val measurementValueRepository: MeasurementValueRepository = inject(),
    private val dateTimeFactory: DateTimeFactory = inject(),
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.Today> {
        val today = dateTimeFactory.today()
        val category = measurementCategoryRepository.getBloodSugar()
        return measurementValueRepository.observeByCategoryId(
            categoryId = category.id,
            minDateTime = today.atStartOfDay(),
            maxDateTime = today.atEndOfDay(),
        ).map { values ->
            DashboardViewState.Revisit.Today(
                totalCount = values.size,
                hypoCount = values.count(MeasurementValue::isTooLow),
                hyperCount = values.count(MeasurementValue::isTooHigh),
            )
        }
    }
}