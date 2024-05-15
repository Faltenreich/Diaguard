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
    ): MeasurementCategory {
        val id = repository.create(
            name = name,
            key = null,
            icon = icon,
            sortIndex = sortIndex,
            isActive = true,
        )
        return checkNotNull(repository.getById(id))
    }
}