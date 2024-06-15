package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.database.sqlite.getLong

class LegacyEntryTagSqliteDao(
    private val database: SqliteDatabase,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun getEntryTags(): List<EntryTag.Legacy> {
        val entryTags = mutableListOf<EntryTag.Legacy>()
        database.query("entrytag") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val entryId = getLong("entry") ?: return@query
            val tagId = getLong("tag") ?: return@query
            entryTags.add(
                EntryTag.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    entryId = entryId,
                    tagId = tagId,
                )
            )
        }
        return entryTags
    }
}