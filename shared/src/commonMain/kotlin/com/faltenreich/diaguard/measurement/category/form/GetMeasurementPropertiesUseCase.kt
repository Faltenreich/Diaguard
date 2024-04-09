package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMeasurementPropertiesUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    operator fun invoke(category: MeasurementCategory): Flow<List<MeasurementProperty>> {
        return combine(
            measurementPropertyRepository.observeByCategoryId(category.id),
            measurementUnitRepository.observeByCategoryId(category.id),
        ) { properties, units ->
            properties.onEach { property ->
                property.units = units
                    .filter { unit -> unit.propertyId == property.id }
                    .onEach { unit -> unit.property = property }
            }
        }
    }
}