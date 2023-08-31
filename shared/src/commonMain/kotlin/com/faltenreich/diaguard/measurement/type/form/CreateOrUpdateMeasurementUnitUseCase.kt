package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject

class CreateOrUpdateMeasurementUnitUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    operator fun invoke(name: String, type: MeasurementType) {
        when (val selectedUnitId = type.selectedUnitId) {
            null -> {
                val unitId = measurementUnitRepository.create(
                    name = name,
                    factor = 1.0,
                    typeId = type.id,
                )
                measurementTypeRepository.update(type.copy(selectedUnitId = unitId))
            }
            else -> measurementUnitRepository.update(
                id = selectedUnitId,
                updatedAt = DateTime.now(),
                name = name,
            )
        }
    }
}