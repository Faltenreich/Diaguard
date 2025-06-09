package com.faltenreich.diaguard.timeline.canvas.chart

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.timeline.date.TimelineDateState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.math.max

class GetTimelineChartStateUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val propertyRepository: MeasurementPropertyRepository,
) {

    operator fun invoke(dateState: TimelineDateState): Flow<TimelineChartState> {
        val dateRange = dateState.current.minus(1, DateUnit.DAY) ..
            dateState.current.plus(1, DateUnit.DAY)
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
                initialDateTime = dateState.initial.atStartOfDay(),
                property = property!!, // TODO: Handle null
                values = values.map { value ->
                    TimelineChartState.Value(
                        dateTime = value.entry.dateTime,
                        value = value.value,
                    )
                },
                valueMin = Y_AXIS_MIN,
                valueLow = property.range.low,
                valueHigh = property.range.high,
                valueMax = max(
                    Y_AXIS_MAX_MIN,
                    (values.maxOfOrNull { it.value } ?: 0.0) + Y_AXIS_STEP,
                ),
                valueStep = Y_AXIS_STEP,
            )
        }
    }


    companion object {

        private const val Y_AXIS_MIN = 0.0
        private const val Y_AXIS_STEP = 50.0
        private const val Y_AXIS_MAX_MIN = 250.0
    }
}