package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnitRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMeasurementTypesUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
    private val measurementTypeUnitRepository: MeasurementTypeUnitRepository = inject(),
) {

    operator fun invoke(property: MeasurementProperty): Flow<List<MeasurementType>> {
        return combine(
            measurementTypeRepository.observeByPropertyId(property.id),
            measurementUnitRepository.observeAll(),
            measurementTypeUnitRepository.observeAll(),
        ) { types, units, typeUnits ->
            types.map { type ->
                type.property = property
                type.units = units.filter { unit ->
                    typeUnits.any { typeUnit -> typeUnit.typeId == type.id && typeUnit.unitId == unit.id }
                }
                // TODO: Get information, e.g. from SharedPreference
                type.selectedUnit = type.units.firstOrNull()
                type
            }
        }
    }
}