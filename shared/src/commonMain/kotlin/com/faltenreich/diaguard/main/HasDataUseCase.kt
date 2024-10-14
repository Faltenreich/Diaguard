package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HasDataUseCase(private val categoryRepository: MeasurementCategoryRepository) {

    operator fun invoke(): Flow<Boolean> {
        return categoryRepository.countAll().map { count -> count > 0 }
    }
}