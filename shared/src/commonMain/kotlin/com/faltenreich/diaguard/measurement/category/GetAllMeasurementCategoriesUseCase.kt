package com.faltenreich.diaguard.measurement.category

import kotlinx.coroutines.flow.Flow

class GetAllMeasurementCategoriesUseCase(
    private val repository: MeasurementCategoryRepository,
) {

    operator fun invoke(): Flow<List<MeasurementCategory.Local>> {
        return repository.observeAll()
    }
}