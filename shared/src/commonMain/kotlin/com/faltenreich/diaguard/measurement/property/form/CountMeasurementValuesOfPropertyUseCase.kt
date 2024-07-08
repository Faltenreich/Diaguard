package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import kotlinx.coroutines.flow.Flow

class CountMeasurementValuesOfPropertyUseCase(
    private val repository: MeasurementValueRepository,
) {

    operator fun invoke(property: MeasurementProperty.Local): Flow<Long> {
        return repository.observeCountByPropertyId(property.id)
    }
}