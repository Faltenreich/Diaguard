package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository

class DeleteMeasurementCategoryUseCase(
    private val repository: MeasurementCategoryRepository,
) {

    operator fun invoke(category: MeasurementCategory.Local) {
        repository.deleteById(category.id)
    }
}