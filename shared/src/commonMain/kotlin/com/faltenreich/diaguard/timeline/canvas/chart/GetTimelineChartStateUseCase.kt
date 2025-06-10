package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions
import com.faltenreich.diaguard.timeline.date.TimelineDateState

class GetTimelineChartStateUseCase {

    operator fun invoke(
        dateState: TimelineDateState,
        values: List<MeasurementValue.Local>,
        dimensions: TimelineCanvasDimensions,
        scrollOffset: Float,
        valueAxis: Iterable<Double> = emptyList(), // TODO: Pass parameter
        xAxis: IntProgression = 0 .. 24 step 2, // TODO: Pass parameter
    ): TimelineChartState {
        return TimelineChartState(
            values = values.takeIf { valueAxis.any() }?.map { value ->
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
            } ?: emptyList(),
        )
    }
}