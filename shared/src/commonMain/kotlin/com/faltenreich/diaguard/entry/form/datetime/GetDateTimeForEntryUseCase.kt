package com.faltenreich.diaguard.entry.form.datetime

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry

class GetDateTimeForEntryUseCase(
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(entry: Entry?, date: Date?): DateTime {
        return entry?.dateTime
            ?: date?.atTime(dateTimeFactory.now().time)
            ?: dateTimeFactory.now()
    }
}