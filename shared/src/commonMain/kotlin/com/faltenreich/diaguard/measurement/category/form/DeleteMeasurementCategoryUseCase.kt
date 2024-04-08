package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.shared.di.inject

class DeleteMeasurementCategoryUseCase(
    private val measurementCategoryRepository: MeasurementCategoryRepository = inject(),
) {

    operator fun invoke(category: MeasurementCategory) {
        measurementCategoryRepository.deleteById(category.id)
    }
}