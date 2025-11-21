package com.faltenreich.diaguard.measurement.category.usecase

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategoryRepository

class DeleteMeasurementCategoryUseCase(
    private val repository: MeasurementCategoryRepository,
) {

    operator fun invoke(category: MeasurementCategory.Local) {
        repository.delete(category)
    }
}