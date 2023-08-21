package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.di.inject

class SetMeasurementPropertyNameUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
) {

    operator fun invoke(property: MeasurementProperty, name: String) {
        measurementPropertyRepository.update(
            id = property.id,
            name = name,
            icon = property.icon,
            sortIndex = property.sortIndex,
        )
    }
}