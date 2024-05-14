package com.faltenreich.diaguard.dashboard.today

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetTodayUseCase(
    private val categoryRepository: MeasurementCategoryRepository = inject(),
    private val valueRepository: MeasurementValueRepository = inject(),
    private val dateTimeFactory: DateTimeFactory = inject(),
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.Today?> {
        val today = dateTimeFactory.today()
        return categoryRepository.observeBloodSugar().flatMapLatest { category ->
            val categoryId = category?.id ?: return@flatMapLatest flowOf(null)
            valueRepository.observeByCategoryId(
                categoryId = categoryId,
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
}