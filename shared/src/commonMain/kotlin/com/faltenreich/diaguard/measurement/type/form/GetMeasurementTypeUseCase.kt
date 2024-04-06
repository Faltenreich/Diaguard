package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMeasurementTypeUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    operator fun invoke(type: MeasurementType): Flow<MeasurementType> {
        return measurementUnitRepository.observeByTypeId(type.id).map { units ->
            type.apply {
                this.property = checkNotNull(measurementPropertyRepository.getById(propertyId))
                this.units = units.onEach { unit ->
                    unit.type = type
                }
                this.selectedUnit = units.first { unit -> unit.id == selectedUnitId }
            }
        }
    }
}