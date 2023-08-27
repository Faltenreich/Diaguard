package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.shared.di.inject

class SetMeasurementTypeSortIndexUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
) {

    operator fun invoke(type: MeasurementType, sortIndex: Long) {
        measurementTypeRepository.update(type.copy(sortIndex = sortIndex))
    }
}