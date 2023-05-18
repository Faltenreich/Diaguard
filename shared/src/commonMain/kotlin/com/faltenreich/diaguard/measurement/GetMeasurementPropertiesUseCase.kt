package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class GetMeasurementPropertiesUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
) {

    operator fun invoke(): Flow<List<MeasurementProperty>> {
        return measurementPropertyRepository.getAll()
    }
}