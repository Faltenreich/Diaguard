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

        // TODO: Adjust according to available screen estate
        val hourProgression = 0 .. DateTimeConstants.HOURS_PER_DAY step 2

        val hours = if (rectangle.size != Size.Zero) {
            val widthPerDay = rectangle.size.width
            val widthPerHour = (widthPerDay / hourProgression.count()).toInt()

            val xOffset = scrollOffset.toInt()
            val xOfFirstHour = xOffset % widthPerHour
            val xOfLastHour = xOfFirstHour + (hourProgression.count() * widthPerHour)
            // Paint one additional hour per side to support cut-off labels
            val (xStart, xEnd) = xOfFirstHour - widthPerHour to xOfLastHour + widthPerHour
            val xOfHours = xStart .. xEnd step widthPerHour

            xOfHours.map { xOfLabel ->
                val xAbsolute = -(xOffset - xOfLabel)
                val xOffsetInHours = xAbsolute / widthPerHour
                val xOffsetInHoursOfDay = ((xOffsetInHours % hourProgression.last) *
                    hourProgression.step) % hourProgression.last
                val hour = when {
                    xOffsetInHoursOfDay < 0 -> xOffsetInHoursOfDay + hourProgression.last
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
            hours = hours,
        )
    }
}