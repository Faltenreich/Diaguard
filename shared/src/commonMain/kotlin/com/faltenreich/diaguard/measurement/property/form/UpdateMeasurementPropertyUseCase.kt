package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository

class UpdateMeasurementPropertyUseCase(
    private val repository: MeasurementPropertyRepository,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(property: MeasurementProperty) = with (property) {
        repository.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
            sortIndex = sortIndex,
            aggregationStyle = aggregationStyle,
            range = range,
            selectedUnitId = selectedUnitId,
        )
    }
}