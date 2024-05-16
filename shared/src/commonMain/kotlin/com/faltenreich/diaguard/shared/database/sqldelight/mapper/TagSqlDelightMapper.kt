package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.tag.Tag

class TagSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        name: String,
    ): Tag.Local {
        return Tag.Local(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            name = name,
        )
    }
}