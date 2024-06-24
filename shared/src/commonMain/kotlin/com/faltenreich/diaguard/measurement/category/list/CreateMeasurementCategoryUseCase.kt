package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.shared.di.inject

class CreateMeasurementCategoryUseCase(
    private val repository: MeasurementCategoryRepository = inject(),
) {

    operator fun invoke(category: MeasurementCategory.User): MeasurementCategory.Local {
        val id = repository.create(category)
        return checkNotNull(repository.getById(id))
    }
}