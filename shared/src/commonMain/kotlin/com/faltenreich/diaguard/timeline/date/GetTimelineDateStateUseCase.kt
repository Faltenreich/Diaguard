package com.faltenreich.diaguard.timeline.date

import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.shared.localization.format
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions

class GetTimelineDateStateUseCase(
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(
        initialDate: Date,
        currentDate: Date,
        dimensions: TimelineCanvasDimensions,
        scrollOffset: Float,
    ): TimelineDateState {
        val rectangle = dimensions.time

        val axis = TimelineDateState.Axis(
            minimum = 0,
            maximum = DateTimeConstants.HOURS_PER_DAY,
            step = 2, // TODO: Adjust according to available screen estate
        )

        val hours = if (rectangle.size != Size.Zero) {
            val widthPerDay = rectangle.size.width
            val widthPerHour = (widthPerDay / axis.progression.count()).toInt()

            val xOffset = scrollOffset.toInt()
            val xOfFirstHour = xOffset % widthPerHour
            val xOfLastHour = xOfFirstHour + (axis.progression.count() * widthPerHour)
            // Paint one additional hour per side to support cut-off labels
            val (xStart, xEnd) = xOfFirstHour - widthPerHour to xOfLastHour + widthPerHour
            val xOfHours = xStart .. xEnd step widthPerHour

            xOfHours.map { xOfLabel ->
                val xAbsolute = -(xOffset - xOfLabel)
                val xOffsetInHours = xAbsolute / widthPerHour
                val xOffsetInHoursOfDay = ((xOffsetInHours % axis.progression.last) *
                    axis.progression.step) % axis.progression.last
                val hour = when {
                    xOffsetInHoursOfDay < 0 -> xOffsetInHoursOfDay + axis.progression.last
                    else -> xOffsetInHoursOfDay
                }
                val x = xOfLabel.toFloat()
                TimelineDateState.Hour(
                    x = x,
                    hour = hour,
                )
            }
        } else {
            emptyList()
        }

        return TimelineDateState(
            rectangle = dimensions.time,
            initialDate = initialDate,
            initialDateTime = initialDate.atStartOfDay(),
            currentDate = currentDate,
            currentDateLocalized = "%s, %s".format(
                dateTimeFormatter.formatDayOfWeek(currentDate, abbreviated = false),
                dateTimeFormatter.formatDate(currentDate),
            ),
            axis = axis,
            hours = hours,
        )
    }
}