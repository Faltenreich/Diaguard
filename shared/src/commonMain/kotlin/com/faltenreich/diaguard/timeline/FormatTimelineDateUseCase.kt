package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.shared.primitive.format

class FormatTimelineDateUseCase(
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(date: Date): String {
        return "%s, %s".format(
            dateTimeFormatter.formatDayOfWeek(date, abbreviated = false),
            dateTimeFormatter.formatDate(date),
        )
    }
}