package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions
import com.faltenreich.diaguard.timeline.canvas.TimelineCoordinates
import com.faltenreich.diaguard.timeline.canvas.step
import com.faltenreich.diaguard.timeline.date.TimelineDateState

class GetTimelineChartStateUseCase {

    operator fun invoke(
        dateState: TimelineDateState,
        values: List<MeasurementValue.Local>,
        dimensions: TimelineCanvasDimensions,
        scrollOffset: Float,
        coordinates: TimelineCoordinates?,
        valueAxis: Iterable<Double> = 0.0 .. 250.0 step 50.0, // TODO: Pass parameter
        xAxis: IntProgression = 0 .. 24 step 2, // TODO: Pass parameter
    ): TimelineChartState {
        val colorStops = mutableListOf<TimelineChartState.ColorStop>()
        coordinates?.valueHigh?.let {
            val yHighFraction = (coordinates.valueHigh - coordinates.valueMin).toFloat() /
                (coordinates.valueMax - coordinates.valueMin).toFloat()
            colorStops.add(TimelineChartState.ColorStop(1 - yHighFraction, TimelineChartState.ColorStop.Type.HIGH))
            colorStops.add(TimelineChartState.ColorStop(1 - yHighFraction, TimelineChartState.ColorStop.Type.NORMAL))
        }
        coordinates?.valueLow?.let {
            val yLowFraction: Float = (coordinates.valueLow - coordinates.valueMin).toFloat() /
                (coordinates.valueMax - coordinates.valueMin).toFloat()
            colorStops.add(TimelineChartState.ColorStop(1 - yLowFraction, TimelineChartState.ColorStop.Type.NORMAL))
            colorStops.add(TimelineChartState.ColorStop(1 - yLowFraction, TimelineChartState.ColorStop.Type.LOW))
        }
        if (colorStops.isEmpty()) {
            colorStops.add(TimelineChartState.ColorStop(0f, TimelineChartState.ColorStop.Type.NORMAL))
            colorStops.add(TimelineChartState.ColorStop(1f, TimelineChartState.ColorStop.Type.NORMAL))
        }

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
            colorStops = colorStops,
        )
    }
}