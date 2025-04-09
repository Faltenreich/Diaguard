package com.faltenreich.diaguard.measurement.unit

class CreateMeasurementUnitUseCase(private val repository: MeasurementUnitRepository) {

    operator fun invoke(
        name: String,
        abbreviation: String,
    ): MeasurementUnit.Local {
        val id = repository.create(
            MeasurementUnit.User(
                name = name,
                abbreviation = abbreviation,
            )
        )
        return checkNotNull(repository.getById(id))
    }
}