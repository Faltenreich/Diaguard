package com.faltenreich.diaguard.measurement.category.usecase

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategoryRepository

class StoreMeasurementCategoryUseCase(private val repository: MeasurementCategoryRepository) {

    operator fun invoke(category: MeasurementCategory): MeasurementCategory.Local {
        val id = when (category) {
            is MeasurementCategory.Seed -> repository.create(category)
            is MeasurementCategory.User -> repository.create(category)
            is MeasurementCategory.Local -> {
                repository.update(category)
                category.id
            }
        }
        return checkNotNull(repository.getById(id))
    }
}