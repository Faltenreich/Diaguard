package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.shared.di.inject

class CreateMeasurementCategoryUseCase(
    private val measurementCategoryRepository: MeasurementCategoryRepository = inject(),
) {

    operator fun invoke(
        name: String,
        key: String?,
        icon: String?,
        sortIndex: Long,
    ) {
        measurementCategoryRepository.create(
            name = name,
            key = key,
            icon = icon,
            sortIndex = sortIndex,
        )
    }
}