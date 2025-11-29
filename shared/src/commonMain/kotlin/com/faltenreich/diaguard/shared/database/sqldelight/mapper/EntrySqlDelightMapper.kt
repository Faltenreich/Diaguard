package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry

class EntrySqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        dateTime: String,
        note: String?,
    ): Entry.Local {
        return Entry.Local(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            dateTime = dateTimeFactory.dateTime(isoString = dateTime),
            note = note,
        )
    }
}