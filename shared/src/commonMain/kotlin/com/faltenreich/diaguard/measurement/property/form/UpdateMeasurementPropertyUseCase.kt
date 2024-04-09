package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository

class UpdateMeasurementPropertyUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository,
) {

    operator fun invoke(property: MeasurementProperty) = with (property) {
        measurementPropertyRepository.update(
            id = id,
            name = name,
            range = range,
            sortIndex = sortIndex,
            selectedUnitId = selectedUnitId,
        )
    }
}