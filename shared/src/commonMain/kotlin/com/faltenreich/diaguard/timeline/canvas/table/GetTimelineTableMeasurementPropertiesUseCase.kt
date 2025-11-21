package com.faltenreich.diaguard.timeline.canvas.table

import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.data.DatabaseKey
import kotlinx.coroutines.flow.Flow

class GetTimelineTableMeasurementPropertiesUseCase(
    private val repository: MeasurementPropertyRepository,
) {

    operator fun invoke(): Flow<List<MeasurementProperty.Local>> {
        return repository.observeIfCategoryIsActive(
            excludedPropertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
        )
    }
}