package com.faltenreich.diaguard.measurement.unit

class UpdateMeasurementUnitUseCase(
    private val measurementUnitRepository: MeasurementUnitRepository,
) {

    operator fun invoke(unit: MeasurementUnit) = with (unit) {
        measurementUnitRepository.update(
            id = id,
            name = name,
            abbreviation = abbreviation,
        )
    }
}