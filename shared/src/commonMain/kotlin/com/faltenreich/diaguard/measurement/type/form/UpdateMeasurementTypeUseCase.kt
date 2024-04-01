package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository

class UpdateMeasurementTypeUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository,
) {

    operator fun invoke(type: MeasurementType) = with (type) {
        measurementTypeRepository.update(
            id = id,
            name = name,
            minimumValue = minimumValue,
            lowValue = lowValue,
            highValue = highValue,
            maximumValue = maximumValue,
            sortIndex = sortIndex,
            selectedUnitId = selectedUnitId,
        )
    }
}