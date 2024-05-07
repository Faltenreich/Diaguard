package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory

class UpdateMeasurementUnitUseCase(
    private val repository: MeasurementUnitRepository,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(unit: MeasurementUnit) = with (unit) {
        repository.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
            abbreviation = abbreviation,
        )
    }
}