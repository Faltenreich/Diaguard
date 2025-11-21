package com.faltenreich.diaguard.measurement.category.usecase

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategoryRepository

class GetMeasurementCategoryByIdUseCase(
    private val repository: MeasurementCategoryRepository,
) {

    operator fun invoke(id: Long): MeasurementCategory.Local? {
        return repository.getById(id)
    }
}