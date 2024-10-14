package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository

class UpdateMeasurementPropertyUseCase(
    private val repository: MeasurementPropertyRepository,
) {

    operator fun invoke(property: MeasurementProperty.Local) {
        repository.update(property)
    }
}