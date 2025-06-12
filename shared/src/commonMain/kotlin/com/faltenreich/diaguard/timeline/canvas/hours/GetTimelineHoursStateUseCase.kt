package com.faltenreich.diaguard.timeline.canvas.hours

import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions

class GetTimelineHoursStateUseCase {

    operator fun invoke(
        initialDate: Date,
        dimensions: TimelineCanvasDimensions,
        scrollOffset: Float,
    ): TimelineHoursState {
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
                TimelineHoursState.Hour(
                    x = x,
                    hour = hour,
                )
            }
        } else {
            emptyList()
        }

        return TimelineHoursState(
            rectangle = rectangle,
            initialDateTime = initialDate.atStartOfDay(),
            hours = hours,
        )
    }
}