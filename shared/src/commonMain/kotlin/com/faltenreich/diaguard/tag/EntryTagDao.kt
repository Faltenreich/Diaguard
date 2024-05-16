package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface EntryTagDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        entryId: Long,
        tagId: Long,
    )

    fun getLastId(): Long?

    fun getByEntryId(entryId: Long): List<EntryTag.Local>

    fun observeByTagId(tagId: Long): Flow<List<EntryTag.Local>>

    fun countByTagId(tagId: Long): Flow<Long>

    fun deleteById(id: Long)
}