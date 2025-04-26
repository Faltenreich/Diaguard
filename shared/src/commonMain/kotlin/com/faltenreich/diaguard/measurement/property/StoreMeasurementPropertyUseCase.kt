package com.faltenreich.diaguard.measurement.property

class StoreMeasurementPropertyUseCase(private val repository: MeasurementPropertyRepository) {

    operator fun invoke(property: MeasurementProperty): MeasurementProperty.Local {
        val id = when (property) {
            is MeasurementProperty.Seed -> repository.create(property)
            is MeasurementProperty.User -> repository.create(property)
            is MeasurementProperty.Local -> {
                repository.update(property)
                property.id
            }
        }
        return checkNotNull(repository.getById(id))
    }
}
