package com.faltenreich.diaguard.timeline.canvas.table

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow

class GetTimelineTableMeasurementPropertiesUseCase(
    private val repository: MeasurementPropertyRepository,
) {

    operator fun invoke(): Flow<List<MeasurementProperty>> {
        return repository.observeIfCategoryIsActive(
            excludedPropertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
        )
    }
}