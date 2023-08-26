package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnitRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMeasurementTypeUnitsUseCase(
    private val measurementTypeUnitRepository: MeasurementTypeUnitRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    operator fun invoke(type: MeasurementType): Flow<List<MeasurementTypeUnit>> {
        return combine(
            measurementTypeUnitRepository.getByTypeId(type.id),
            measurementUnitRepository.observeAll(),
        ) { typeUnits, units ->
            typeUnits.map { typeUnit ->
                typeUnit.apply {
                    this.unit = units.first { it.id == typeUnit.unitId }
                    this.type = type
                }
            }
        }
    }
}