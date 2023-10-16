package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.di.inject

class CreateMeasurementPropertyUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
) {

    operator fun invoke(
        name: String,
        key: String?,
        icon: String?,
        sortIndex: Long,
    ) {
        measurementPropertyRepository.create(
            name = name,
            key = key,
            icon = icon,
            sortIndex = sortIndex,
        )
    }
}