package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnitRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMeasurementPropertiesUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
    private val measurementTypeUnitRepository: MeasurementTypeUnitRepository = inject(),
) {

    operator fun invoke(): Flow<List<MeasurementProperty>> {
        return combine(
            measurementPropertyRepository.observeAll(),
            measurementTypeRepository.observeAll(),
            measurementUnitRepository.observeAll(),
            measurementTypeUnitRepository.observeAll(),
        ) { properties, types, units, typeUnits ->
            properties.map { property ->
                property.apply {
                    this.types = types.filter { type ->
                        when (type.propertyId) {
                            property.id -> {
                                type.property = property
                                type.typeUnits = typeUnits
                                    .filter { typeUnit -> typeUnit.typeId == type.id }
                                    .map { typeUnit ->
                                        typeUnit.type = type
                                        typeUnit.unit = units.first { unit -> unit.id == typeUnit.unitId }
                                        typeUnit
                                    }
                                type.selectedTypeUnit = typeUnits.firstOrNull { typeUnit -> typeUnit.id == type.selectedTypeUnitId }
                                true
                            }
                            else -> false
                        }

                    }
                }
            }
        }
    }
}