package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.di.inject

class CreateMeasurementPropertyUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
) {

    operator fun invoke(
        name: String,
        icon: String?,
        sortIndex: Long,
    ) {
        measurementPropertyRepository.create(
            name = name,
            icon = icon,
            sortIndex = sortIndex,
        )
    }
}