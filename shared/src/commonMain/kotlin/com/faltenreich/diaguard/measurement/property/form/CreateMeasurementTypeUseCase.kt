package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.shared.di.inject

class CreateMeasurementTypeUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
) {

    operator fun invoke(
        name: String,
        sortIndex: Long,
        propertyId: Long,
    ) {
        measurementTypeRepository.create(
            name = name,
            sortIndex = sortIndex,
            propertyId = propertyId,
        )
    }
}