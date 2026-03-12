package com.faltenreich.diaguard.measurement.property.usecase

import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository

class GetMaximumSortIndexOfMeasurementPropertyUseCase(private val propertyRepository: MeasurementPropertyRepository) {

    operator fun invoke(categoryId: Long): Long? {
        return propertyRepository.getMaximumSortIndex(categoryId)
    }
}