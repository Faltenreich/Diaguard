package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.shared.datetime.Date

sealed class LogViewState(val date: Date) {

    class Requesting(initialDate: Date) : LogViewState(date = initialDate)

    class Responding(
        currentDate: Date,
        val data: List<LogData>,
    ) : LogViewState(date = currentDate)
}