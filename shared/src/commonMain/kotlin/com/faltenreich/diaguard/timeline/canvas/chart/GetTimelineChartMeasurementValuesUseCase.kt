package com.faltenreich.diaguard.timeline.canvas.chart

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.timeline.date.TimelineDateState
import kotlinx.coroutines.flow.Flow

class GetTimelineChartMeasurementValuesUseCase(
    private val repository: MeasurementValueRepository,
) {

    operator fun invoke(dateState: TimelineDateState): Flow<List<MeasurementValue.Local>> {
        val dateRange = dateState.currentDate.minus(1, DateUnit.DAY) ..
            dateState.currentDate.plus(1, DateUnit.DAY)
        return repository.observeByDateRange(
            startDateTime = dateRange.start.atStartOfDay(),
            endDateTime = dateRange.endInclusive.atEndOfDay(),
            propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
        )
    }
}