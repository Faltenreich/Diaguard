package com.faltenreich.diaguard.timeline.canvas.chart

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.timeline.date.TimelineDateState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTimelineChartStateUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val propertyRepository: MeasurementPropertyRepository,
) {

    operator fun invoke(dateState: TimelineDateState): Flow<TimelineChartState> {
        val dateRange = dateState.currentDate.minus(1, DateUnit.DAY) ..
            dateState.currentDate.plus(1, DateUnit.DAY)
        val propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR
        return combine(
            valueRepository.observeByDateRange(
                startDateTime = dateRange.start.atStartOfDay(),
                endDateTime = dateRange.endInclusive.atEndOfDay(),
                propertyKey = propertyKey,
            ),
            propertyRepository.observeByKey(propertyKey),
        ) { values, property ->
            TimelineChartState(
                initialDateTime = dateState.initialDate.atStartOfDay(),
                property = property!!, // TODO: Handle null
                values = values.map { value ->
                    TimelineChartState.Value(
                        dateTime = value.entry.dateTime,
                        value = value.value,
                    )
                },
            )
        }
    }
}