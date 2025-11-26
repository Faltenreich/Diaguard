package com.faltenreich.diaguard.measurement.category.usecase

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategoryRepository

class GetMeasurementCategoryByIdUseCase(
    private val repository: MeasurementCategoryRepository,
    private val localize: LocalizeMeasurementCategoryUseCase,
) {

    operator fun invoke(id: Long): MeasurementCategory.Localized? {
        return repository.getById(id)?.let(localize::invoke)
    }
}