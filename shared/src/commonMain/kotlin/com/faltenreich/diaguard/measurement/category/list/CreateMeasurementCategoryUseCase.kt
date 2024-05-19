package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository

class CreateMeasurementCategoryUseCase(
    private val repository: MeasurementCategoryRepository,
) {

    operator fun invoke(
        name: String,
        icon: String?,
        sortIndex: Long,
    ): MeasurementCategory.Local {
        val category = MeasurementCategory.User(
            name = name,
            icon = icon,
            sortIndex = sortIndex,
            isActive = true,
        )
        val id = repository.create(category)
        return checkNotNull(repository.getById(id))
    }
}