package com.faltenreich.diaguard.measurement.unit

class UpdateMeasurementUnitUseCase(private val repository: MeasurementUnitRepository) {

    operator fun invoke(unit: MeasurementUnit.Local) {
        repository.update(unit)
    }
}