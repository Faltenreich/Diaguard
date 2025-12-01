package com.faltenreich.diaguard.measurement.category.usecase

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategoryRepository
import kotlinx.coroutines.flow.Flow

class GetMeasurementCategoriesUseCase(
    private val repository: MeasurementCategoryRepository,
) {

    operator fun invoke(): Flow<List<MeasurementCategory.Local>> {
        return repository.observeAll()
    }
}