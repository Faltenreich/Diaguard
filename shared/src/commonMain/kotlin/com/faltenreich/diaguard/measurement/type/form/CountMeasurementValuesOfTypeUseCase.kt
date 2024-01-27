package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class CountMeasurementValuesOfTypeUseCase(
    private val measurementValueRepository: MeasurementValueRepository = inject(),
) {

    operator fun invoke(measurementTypeId: Long): Flow<Long> {
        return measurementValueRepository.observeCountByTypeId(measurementTypeId)
    }
}