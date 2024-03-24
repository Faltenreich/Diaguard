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

    fun getByEntryId(entryId: Long): List<EntryTag>

    fun observeByEntryId(entryId: Long): Flow<List<EntryTag>>

    fun observeByTagId(tagId: Long): Flow<List<EntryTag>>

    fun countByTagId(tagId: Long): Flow<Long>

    fun deleteById(id: Long)
}