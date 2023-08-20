package com.faltenreich.diaguard.measurement.type.list

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.shared.di.inject

class SetMeasurementTypeSortIndexUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
) {

    operator fun invoke(type: MeasurementType, sortIndex: Long) {
        measurementTypeRepository.update(
            id = type.id,
            name = type.name,
            sortIndex = sortIndex,
            selectedUnitId = type.selectedUnitId,
        )
    }
}