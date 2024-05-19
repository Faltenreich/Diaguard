package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.di.inject

class DeleteMeasurementPropertyUseCase(
    private val repository: MeasurementPropertyRepository = inject(),
) {

    operator fun invoke(property: MeasurementProperty.Local) {
        repository.deleteById(property.id)
    }
}