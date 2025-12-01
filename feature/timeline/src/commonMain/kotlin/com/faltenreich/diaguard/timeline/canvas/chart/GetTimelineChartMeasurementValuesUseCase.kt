package com.faltenreich.diaguard.timeline.canvas.chart

import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import kotlinx.coroutines.flow.Flow

class GetTimelineChartMeasurementValuesUseCase(
    private val repository: MeasurementValueRepository,
) {

    operator fun invoke(date: Date): Flow<List<MeasurementValue.Local>> {
        return repository.observeByDateRange(
            startDateTime = date.minus(1, DateUnit.DAY).atStartOfDay(),
            endDateTime = date.plus(1, DateUnit.DAY).atEndOfDay(),
            propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
        )
    }
}