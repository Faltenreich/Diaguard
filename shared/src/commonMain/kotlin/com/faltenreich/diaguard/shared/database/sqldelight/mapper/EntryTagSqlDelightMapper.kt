package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.tag.EntryTag

class EntryTagSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val entryMapper: EntrySqlDelightMapper,
    private val tagMapper: TagSqlDelightMapper,
) {

    fun map(
        entryTagId: Long,
        entryTagCreatedAt: String,
        entryTagUpdatedAt: String,
        @Suppress("UNUSED_PARAMETER") entryTagEntryId: Long,
        @Suppress("UNUSED_PARAMETER") entryTagTagId: Long,
        entryId: Long,
        entryCreatedAt: String,
        entryUpdatedAt: String,
        entryDateTime: String,
        entryNote: String?,

        tagId: Long,
        tagCreatedAt: String,
        tagUpdatedAt: String,
        tagName: String,
    ): EntryTag.Local {
        return EntryTag.Local(
            id = entryTagId,
            createdAt = dateTimeFactory.dateTime(isoString = entryTagCreatedAt),
            updatedAt = dateTimeFactory.dateTime(isoString = entryTagUpdatedAt),
            entry = entryMapper.map(
                id = entryId,
                createdAt = entryCreatedAt,
                updatedAt = entryUpdatedAt,
                dateTime = entryDateTime,
                note = entryNote,
            ),
            tag = tagMapper.map(
                id = tagId,
                createdAt = tagCreatedAt,
                updatedAt = tagUpdatedAt,
                name = tagName,
            ),
        )
    }
}