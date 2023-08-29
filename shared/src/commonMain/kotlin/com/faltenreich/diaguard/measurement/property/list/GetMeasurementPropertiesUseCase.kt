package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMeasurementPropertiesUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    operator fun invoke(): Flow<List<MeasurementProperty>> {
        return combine(
            measurementPropertyRepository.observeAll(),
            measurementTypeRepository.observeAll(),
            measurementUnitRepository.observeAll(),
        ) { properties, types, units ->
            properties.map { property ->
                property.apply {
                    this.types = types.filter { type ->
                        when (type.propertyId) {
                            property.id -> {
                                type.property = property
                                type.units = units
                                    .filter { unit -> unit.typeId == type.id }
                                    .map { unit ->
                                        unit.type = type
                                        unit
                                    }
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