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
                                type.units = units.filter { unit ->
                                    typeUnits.any { typeUnit -> typeUnit.typeId == type.id && typeUnit.unitId == unit.id }
                                }
                                // TODO: Get information, e.g. from SharedPreference
                                type.selectedUnit = type.units.first()
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