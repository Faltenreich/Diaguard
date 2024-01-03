package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class EntryTagRepository(
    private val dao: EntryTagDao,
) {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        entryId: Long,
        tagId: Long,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            entryId = entryId,
            tagId = tagId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getLastId(): Long? {
        return dao.getLastId()
    }

    fun observeByEntryId(entryId: Long): Flow<List<EntryTag>> {
        return dao.observeByEntryId(entryId)
    }

    fun observeByTagId(tagId: Long): Flow<List<EntryTag>> {
        return dao.observeByTagId(tagId)
    }

    fun countByTagId(tagId: Long): Flow<Long> {
        return dao.countByTagId(tagId)
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}