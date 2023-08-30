package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMeasurementTypeUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    operator fun invoke(measurementTypeId: Long): Flow<MeasurementType?> {
        return combine(
            measurementTypeRepository.observeById(measurementTypeId),
            measurementUnitRepository.getByTypeId(measurementTypeId),
        ) { type, units ->
            type?.apply {
                this.units = units.onEach { unit ->
                    unit.type = type
                }
            }
        }
    }
}