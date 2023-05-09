package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.datetime.Date

sealed class TimelineViewState(val date: Date) {

    class Requesting(date: Date) : TimelineViewState(date)

    class Responding(
        date: Date,
        val entries: List<Entry> = emptyList(),
    ) : TimelineViewState(date)
}