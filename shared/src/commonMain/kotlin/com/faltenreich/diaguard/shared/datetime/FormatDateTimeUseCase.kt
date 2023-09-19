package com.faltenreich.diaguard.shared.datetime

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