package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import kotlinx.coroutines.flow.Flow

class GetMeasurementUnitsOfPropertyUseCase(
    private val repository: MeasurementUnitRepository,
) {

    operator fun invoke(property: MeasurementProperty.Local): Flow<List<MeasurementUnit.Local>> {
        return repository.observeByPropertyId(property.id)
    }
}