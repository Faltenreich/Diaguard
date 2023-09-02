package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.shared.di.inject

class UpdateMeasurementTypeUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
) {

    operator fun invoke(type: MeasurementType) {
        measurementTypeRepository.update(
            id = type.id,
            name = type.name,
            sortIndex = type.sortIndex,
            selectedUnitId = type.selectedUnitId,
        )
    }
}