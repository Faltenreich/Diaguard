package com.faltenreich.diaguard.onboarding

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HasDataUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
) {

    operator fun invoke(): Flow<Boolean> {
        return measurementPropertyRepository.getAll().map(List<*>::isEmpty)
    }
}