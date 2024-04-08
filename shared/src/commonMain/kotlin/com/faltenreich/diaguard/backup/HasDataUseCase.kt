package com.faltenreich.diaguard.backup

import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HasDataUseCase(
    private val measurementCategoryRepository: MeasurementCategoryRepository = inject(),
) {

    operator fun invoke(): Flow<Boolean> {
        return measurementCategoryRepository.countAll().map { count -> count > 0 }
    }
}