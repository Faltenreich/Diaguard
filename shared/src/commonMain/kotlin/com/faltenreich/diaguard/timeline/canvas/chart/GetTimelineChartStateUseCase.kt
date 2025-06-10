package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions
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
                values = emptyList(),
            )
        }
    }
}

private fun something(
    dateState: TimelineDateState,
    values: List<MeasurementValue.Local>,
    dimensions: TimelineCanvasDimensions,
    scrollOffset: Float,
    valueAxis: Iterable<Double>,
    xAxis: IntProgression,
): List<Offset> {
    return values.map { value ->
        val dateTime = value.entry.dateTime

        val widthPerDay = dimensions.chart.size.width
        val widthPerHour = widthPerDay / (xAxis.last / xAxis.step)
        val widthPerMinute = widthPerHour / DateTimeConstants.MINUTES_PER_HOUR
        val offsetInMinutes = dateState.initialDateTime.minutesUntil(dateTime)
        val offsetOfDateTime = (offsetInMinutes / xAxis.step) * widthPerMinute
        val x = dimensions.chart.topLeft.x + scrollOffset + offsetOfDateTime

        val percentage = (value.value - valueAxis.first()) /
            (valueAxis.last() - valueAxis.first())
        val y = dimensions.chart.topLeft.y +
            dimensions.chart.size.height -
            (percentage.toFloat() * dimensions.chart.size.height)

        Offset(x, y)
    }
}