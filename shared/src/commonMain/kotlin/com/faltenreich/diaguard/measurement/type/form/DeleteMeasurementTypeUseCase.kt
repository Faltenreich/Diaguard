package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.shared.di.inject

class DeleteMeasurementTypeUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
) {

    operator fun invoke(measurementType: MeasurementType) {
        measurementTypeRepository.deleteById(measurementType.id)
    }
}