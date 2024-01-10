package com.faltenreich.diaguard.entry.form.datetime

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject

class GetDateTimeForEntryUseCase(
    private val dateTimeFactory: DateTimeFactory = inject(),
) {

    operator fun invoke(entry: Entry?, date: Date?): DateTime {
        return entry?.dateTime
            ?: date?.atTime(dateTimeFactory.now().time)
            ?: dateTimeFactory.now()
    }
}