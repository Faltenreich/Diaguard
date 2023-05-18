package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.datetime.DateTime

class EntrySqlDelightMapper {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        dateTime: String,
        note: String?,
    ): Entry {
        return Entry(
            id = id,
            createdAt = DateTime(isoString = createdAt),
            updatedAt = DateTime(isoString = updatedAt),
            dateTime = DateTime(isoString = dateTime),
            note = note,
        )
    }
}