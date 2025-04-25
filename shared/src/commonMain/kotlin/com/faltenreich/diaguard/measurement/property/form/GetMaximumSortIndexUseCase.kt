package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository

class GetMaximumSortIndexUseCase(private val propertyRepository: MeasurementPropertyRepository) {

    operator fun invoke(categoryId: Long): Long? {
        return propertyRepository.getMaximumSortIndex(categoryId)
    }
}