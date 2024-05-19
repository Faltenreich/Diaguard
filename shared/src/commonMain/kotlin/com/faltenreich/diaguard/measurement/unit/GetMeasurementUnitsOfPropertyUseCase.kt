package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import kotlinx.coroutines.flow.Flow

class GetMeasurementUnitsOfPropertyUseCase {

    operator fun invoke(property: MeasurementProperty.Local): Flow<List<MeasurementUnit.Local>> {
        TODO()
    }
}