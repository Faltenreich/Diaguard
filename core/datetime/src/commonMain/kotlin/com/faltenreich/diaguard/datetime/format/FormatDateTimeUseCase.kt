package com.faltenreich.diaguard.datetime.format

class FormatDateTimeUseCase(
    private val dateTimeFormatter: DateTimeFormatter,
) : DateTimeFormatter by dateTimeFormatter