package com.faltenreich.diaguard.measurement.unit

class StoreMeasurementUnitUseCase(private val repository: MeasurementUnitRepository) {

    operator fun invoke(unit: MeasurementUnit): MeasurementUnit.Local {
        val id = when (unit) {
            is MeasurementUnit.Seed -> repository.create(unit)
            is MeasurementUnit.User -> repository.create(unit)
            is MeasurementUnit.Local -> {
                repository.update(unit)
                unit.id
            }
        }
        return checkNotNull(repository.getById(id))
    }
}