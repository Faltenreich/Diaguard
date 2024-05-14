package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.shared.di.inject

class UpdateMeasurementCategoryUseCase(
    private val repository: MeasurementCategoryRepository = inject(),
) {

    operator fun invoke(category: MeasurementCategory) = with(category) {
        repository.update(
            id = id,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
            isActive = isActive,
        )
    }
}