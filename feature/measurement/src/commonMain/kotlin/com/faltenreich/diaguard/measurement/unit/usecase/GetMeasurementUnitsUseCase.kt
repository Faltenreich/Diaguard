package com.faltenreich.diaguard.measurement.unit.usecase

import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMeasurementUnitsUseCase(
    private val repository: MeasurementUnitRepository,
) {

    operator fun invoke(): Flow<List<MeasurementUnit.Local>> {
        return repository.observeAll().map { it.sortedBy(MeasurementUnit::name) }
    }
}