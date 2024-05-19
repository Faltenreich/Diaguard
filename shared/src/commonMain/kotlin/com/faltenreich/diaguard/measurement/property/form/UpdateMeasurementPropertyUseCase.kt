package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository

class UpdateMeasurementPropertyUseCase(
    private val repository: MeasurementPropertyRepository,
) {

    operator fun invoke(property: MeasurementProperty.Local) = with(property) {
        repository.update(
            id = id,
            name = name,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
            range = range,
        )
    }
}