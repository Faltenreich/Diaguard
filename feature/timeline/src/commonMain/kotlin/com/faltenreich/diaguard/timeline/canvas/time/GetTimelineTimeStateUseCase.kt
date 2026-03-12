package com.faltenreich.diaguard.timeline.canvas.time

import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.time_midnight
import com.faltenreich.diaguard.resource.time_noon
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions
import com.faltenreich.diaguard.timeline.date.TimelineDateState

class GetTimelineTimeStateUseCase(
    private val localization: Localization,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(
        dateState: TimelineDateState,
        dimensions: TimelineCanvasDimensions.Calculated?,
    ): TimelineTimeState? {
        if (dimensions == null) {
            return null
        }

        val xMin = 0
        val xMax = DateTimeConstants.HOURS_PER_DAY
        val xStep = STEP
        val xRange = xMin .. xMax
        val xAxis = xRange step xStep
        val xAxisLabelCount = xRange.last / xAxis.step

        val rectangle = dimensions.time
        val hours = if (rectangle.size != Size.Zero) {
            val widthPerDay = rectangle.size.width
            val widthPerHour = (widthPerDay / xAxisLabelCount).toInt()

            val xOffset = dimensions.scroll.toInt()
            val xOfFirstHour = xOffset % widthPerHour
            val xOfLastHour = xOfFirstHour + (xAxisLabelCount * widthPerHour)
            // Paint one additional hour per side to support cut-off labels
            val (xStart, xEnd) = xOfFirstHour - widthPerHour to xOfLastHour + widthPerHour
            val xOfHours = xStart .. xEnd step widthPerHour

            xOfHours.map { xOfLabel ->
                val xAbsolute = -(xOffset - xOfLabel)
                val xOffsetInHours = xAbsolute / widthPerHour
                val xOffsetInHoursOfDay = ((xOffsetInHours % xAxis.last) * xAxis.step) % xAxis.last
                val hour = when {
                    xOffsetInHoursOfDay < 0 -> xOffsetInHoursOfDay + xAxis.last
                    else -> xOffsetInHoursOfDay
                }
                val hourLocalized = if (dateTimeFormatter.is24HourFormat()) {
                    hour.toString()
                } else {
                    when (hour) {
                        HOUR_MIDNIGHT -> localization.getString(Res.string.time_midnight)
                        HOUR_NOON -> localization.getString(Res.string.time_noon)
                        else -> (hour % HOUR_NOON).toString()
                    }
                }
                val x = xOfLabel.toFloat()
                TimelineTimeState.Hour(
                    x = x,
                    hour = hour,
                    hourLocalized = hourLocalized,
                )
            }
        } else {
            emptyList()
        }

        return TimelineTimeState(
            dimensions = dimensions,
            currentDate = dateState.currentDate,
            initialDateTime = dateState.initialDate.atStartOfDay(),
            hourProgression = xAxis,
            hours = hours,
        )
    }

    companion object {

        private const val STEP = 2
        private const val HOUR_MIDNIGHT = 0
        private const val HOUR_NOON = 12
    }
}