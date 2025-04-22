package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository

class CreateMeasurementCategoryUseCase(
    private val repository: MeasurementCategoryRepository,
) {

    operator fun invoke(category: MeasurementCategory.User): MeasurementCategory.Local {
        val id = repository.create(category)
        return checkNotNull(repository.getById(id))
    }
}