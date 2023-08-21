package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.di.inject

class SetMeasurementPropertyIconUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
) {

    operator fun invoke(property: MeasurementProperty, icon: String?) {
        measurementPropertyRepository.update(
            id = property.id,
            name = property.name,
            icon = icon,
            sortIndex = property.sortIndex,
        )
    }
}