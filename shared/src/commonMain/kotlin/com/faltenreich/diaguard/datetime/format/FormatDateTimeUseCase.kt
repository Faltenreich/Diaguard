package com.faltenreich.diaguard.datetime.format

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.Time

class FormatDateTimeUseCase(private val dateTimeFormatter: DateTimeFormatter) {

    operator fun invoke(dateTime: DateTime): String {
        return dateTimeFormatter.formatDateTime(dateTime)
    }

    operator fun invoke(date: Date): String {
        return dateTimeFormatter.formatDate(date)
    }

    operator fun invoke(time: Time): String {
        return dateTimeFormatter.formatTime(time)
    }
}