package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class GetMeasurementCategoriesUseCase(
    private val repository: MeasurementCategoryRepository = inject(),
) {

    operator fun invoke(): Flow<List<MeasurementCategory>> {
        return repository.observeAll()
    }
}