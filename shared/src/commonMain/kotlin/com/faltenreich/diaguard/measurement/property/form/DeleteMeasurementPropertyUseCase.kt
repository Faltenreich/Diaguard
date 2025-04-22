package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository

class DeleteMeasurementPropertyUseCase(
    private val repository: MeasurementPropertyRepository,
) {

    operator fun invoke(property: MeasurementProperty.Local) {
        repository.delete(property)
    }
}