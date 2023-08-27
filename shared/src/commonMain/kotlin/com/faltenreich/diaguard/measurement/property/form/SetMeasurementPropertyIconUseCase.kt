package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.di.inject

class SetMeasurementPropertyIconUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
) {

    operator fun invoke(property: MeasurementProperty, icon: String?) {
        measurementPropertyRepository.update(property.copy(icon = icon))
    }
}