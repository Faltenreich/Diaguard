package com.faltenreich.diaguard.timeline.canvas.chart

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTimelineChartMeasurementPropertyUseCase(
    private val repository: MeasurementPropertyRepository,
) {

    operator fun invoke(): Flow<MeasurementProperty.Local> {
        return repository
            .observeByKey(DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
            .map(::checkNotNull)
    }
}