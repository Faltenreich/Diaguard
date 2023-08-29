package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMeasurementUnitsUseCase(
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    operator fun invoke(type: MeasurementType): Flow<List<MeasurementUnit>> {
        return measurementUnitRepository.getByTypeId(type.id).map { units ->
            units.map { unit ->
                unit.type = type
                unit
            }
        }
    }
}