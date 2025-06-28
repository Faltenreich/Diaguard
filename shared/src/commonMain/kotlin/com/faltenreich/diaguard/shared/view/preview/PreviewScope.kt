package com.faltenreich.diaguard.shared.view.preview

import com.faltenreich.diaguard.datetime.DayOfWeek
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry

class PreviewScope(
    dateTimeFactory: DateTimeFactory,
) : DateTimeFactory by dateTimeFactory {

    fun entry() = Entry.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        dateTime = now(),
        note = "note",
    )

    @Suppress("MagicNumber")
    fun DayOfWeek.localized() = toString()
        .take(3)
        .lowercase()
        .replaceFirstChar(Char::uppercase)
}