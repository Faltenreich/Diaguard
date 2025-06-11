package com.faltenreich.diaguard.timeline.date

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
        return TimelineDateState(
            rectangle = dimensions.time,
            initialDate = initialDate,
            initialDateTime = initialDate.atStartOfDay(),
            currentDate = currentDate,
            currentDateLocalized = "%s, %s".format(
                dateTimeFormatter.formatDayOfWeek(currentDate, abbreviated = false),
                dateTimeFormatter.formatDate(currentDate),
            ),
            axis = TimelineDateState.Axis(
                minimum = 0,
                maximum = DateTimeConstants.HOURS_PER_DAY,
                step = 2, // TODO: Adjust according to available screen estate
            ),
            // TODO: Remove by calculating offsets beforehand
            scrollOffset = scrollOffset,
        )
    }
}