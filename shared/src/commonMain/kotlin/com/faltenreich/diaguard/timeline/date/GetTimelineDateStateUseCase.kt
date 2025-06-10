package com.faltenreich.diaguard.timeline.date

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.shared.localization.format

class GetTimelineDateStateUseCase(
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(
        initialDate: Date,
        currentDate: Date,
    ): TimelineDateState {
        return TimelineDateState(
            initialDate = initialDate,
            initialDateTime = initialDate.atStartOfDay(),
            currentDate = currentDate,
            currentDateLocalized = "%s, %s".format(
                dateTimeFormatter.formatDayOfWeek(currentDate, abbreviated = false),
                dateTimeFormatter.formatDate(currentDate),
            ),
        )
    }
}