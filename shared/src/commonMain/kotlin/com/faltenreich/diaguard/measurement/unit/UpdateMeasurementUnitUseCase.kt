package com.faltenreich.diaguard.measurement.unit

class UpdateMeasurementUnitUseCase(private val repository: MeasurementUnitRepository) {

    operator fun invoke(unit: MeasurementUnit.Local) = with(unit) {
        repository.update(
            id = id,
            name = name,
            abbreviation = abbreviation,
            isSelected = isSelected,
        )
    }
}