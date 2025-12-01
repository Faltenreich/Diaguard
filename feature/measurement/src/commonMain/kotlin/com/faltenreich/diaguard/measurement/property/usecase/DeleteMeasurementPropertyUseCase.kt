package com.faltenreich.diaguard.measurement.property.usecase

import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository

class DeleteMeasurementPropertyUseCase(
    private val repository: MeasurementPropertyRepository,
) {

    operator fun invoke(property: MeasurementProperty.Local) {
        repository.delete(property)
    }
}