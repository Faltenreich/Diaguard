package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.di.inject

class SetMeasurementPropertySortIndexUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
) {

    operator fun invoke(property: MeasurementProperty, sortIndex: Long) {
        measurementPropertyRepository.update(property.copy(sortIndex = sortIndex))
    }
}