package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMeasurementTypesUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    operator fun invoke(category: MeasurementCategory): Flow<List<MeasurementType>> {
        return combine(
            measurementTypeRepository.observeByCategoryId(category.id),
            measurementUnitRepository.observeByCategoryId(category.id),
        ) { types, units ->
            types.onEach { type ->
                type.units = units
                    .filter { unit -> unit.typeId == type.id }
                    .onEach { unit -> unit.type = type }
            }
        }
    }
}