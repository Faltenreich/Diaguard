package com.faltenreich.diaguard.measurement.category

import kotlinx.coroutines.flow.Flow

class GetActiveMeasurementCategoriesUseCase(
    private val repository: MeasurementCategoryRepository,
) {

    operator fun invoke(): Flow<List<MeasurementCategory>> {
        return repository.observeActive()
    }
}