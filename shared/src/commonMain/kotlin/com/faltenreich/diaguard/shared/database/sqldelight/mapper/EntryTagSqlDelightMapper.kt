package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
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
        entryTagEntryId: Long,
        entryTagTagId: Long,
        entryId: Long,
        entryCreatedAt: String,
        entryUpdatedAt: String,
        entryDateTime: String,
        entryNote: String?,
        tagId: Long,
        tagCreatedAt: String,
        tagUpdatedAt: String,
        tagName: String,
    ): EntryTag {
        return EntryTag(
            id = entryTagId,
            createdAt = dateTimeFactory.dateTime(isoString = entryTagCreatedAt),
            updatedAt = dateTimeFactory.dateTime(isoString = entryTagUpdatedAt),
            entryId = entryTagEntryId,
            tagId = entryTagTagId,
        ).apply {
            entry = entryMapper.map(
                id = entryId,
                createdAt = entryCreatedAt,
                updatedAt = entryUpdatedAt,
                dateTime = entryDateTime,
                note = entryNote,
            )
            tag = tagMapper.map(
                id = tagId,
                createdAt = tagCreatedAt,
                updatedAt = tagUpdatedAt,
                name = tagName,
            )
        }
    }
}