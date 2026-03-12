package com.faltenreich.diaguard.measurement.property.usecase

import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository

class GetMeasurementPropertyBdIdUseCase(
    private val repository: MeasurementPropertyRepository,
) {

    operator fun invoke(id: Long): MeasurementProperty.Local? {
        return repository.getById(id)
    }
}