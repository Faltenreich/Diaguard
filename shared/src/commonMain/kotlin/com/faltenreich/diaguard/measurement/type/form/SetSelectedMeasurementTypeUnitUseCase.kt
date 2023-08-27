package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnit
import com.faltenreich.diaguard.shared.di.inject

class SetSelectedMeasurementTypeUnitUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
) {

    operator fun invoke(
        type: MeasurementType,
        typeUnit: MeasurementTypeUnit,
    ) {
        measurementTypeRepository.update(type.copy(selectedTypeUnitId = typeUnit.id))
    }
}