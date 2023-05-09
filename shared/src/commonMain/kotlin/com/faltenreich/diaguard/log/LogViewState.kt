package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.datetime.Date

sealed class LogViewState(val date: Date) {

    class Requesting(date: Date) : LogViewState(date)

    class Responding(
        date: Date,
        val entries: List<Entry> = emptyList(),
    ) : LogViewState(date)
}