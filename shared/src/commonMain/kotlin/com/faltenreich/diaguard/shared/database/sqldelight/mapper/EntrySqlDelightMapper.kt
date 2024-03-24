package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory

class EntrySqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        dateTime: String,
        note: String?,
    ): Entry {
        return Entry(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            dateTime = dateTimeFactory.dateTime(isoString = dateTime),
            note = note,
        )
    }
}