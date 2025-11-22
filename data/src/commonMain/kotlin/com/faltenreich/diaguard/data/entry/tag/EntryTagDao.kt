package com.faltenreich.diaguard.data.entry.tag

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

internal interface EntryTagDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        entryId: Long,
        tagId: Long,
    )

    fun getLastId(): Long?

    fun getByEntryId(entryId: Long): List<EntryTag.Local>

    fun observeByEntryId(entryId: Long): Flow<List<EntryTag.Local>>

    fun countByTagId(tagId: Long): Flow<Long>

    fun deleteById(id: Long)
}