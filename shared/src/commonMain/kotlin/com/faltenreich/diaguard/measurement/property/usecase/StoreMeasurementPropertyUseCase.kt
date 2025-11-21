package com.faltenreich.diaguard.measurement.property.usecase

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.architecture.Result

class StoreMeasurementPropertyUseCase(private val repository: MeasurementPropertyRepository) {

    operator fun invoke(property: MeasurementProperty): Result<MeasurementProperty.Local, Unit> {
        val id = when (property) {
            is MeasurementProperty.Seed -> repository.create(property)
            is MeasurementProperty.User -> repository.create(property)
            is MeasurementProperty.Local -> {
                repository.update(property)
                property.id
            }
        } ?: return Result.Failure(Unit)
        return repository.getById(id)?.let { Result.Success(it) } ?: Result.Failure(Unit)
    }
}