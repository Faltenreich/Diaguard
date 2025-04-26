package com.faltenreich.diaguard.measurement.category

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