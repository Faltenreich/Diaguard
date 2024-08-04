package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository

class GetMeasurementPropertyBdIdUseCase(
    private val repository: MeasurementPropertyRepository,
) {

    operator fun invoke(id: Long): MeasurementProperty.Local? {
        return repository.getById(id)
    }
}