package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import kotlinx.coroutines.flow.Flow

class GetMeasurementUnitsUseCase(
    private val repository: MeasurementUnitRepository,
) {

    operator fun invoke(): Flow<List<MeasurementUnit.Local>> {
        return repository.observeAll()
    }
}