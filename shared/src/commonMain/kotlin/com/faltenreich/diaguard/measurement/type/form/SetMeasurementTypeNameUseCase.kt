package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.shared.di.inject

class SetMeasurementTypeNameUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
) {

    operator fun invoke(type: MeasurementType, name: String) {
        measurementTypeRepository.update(type.copy(name = name))
    }
}