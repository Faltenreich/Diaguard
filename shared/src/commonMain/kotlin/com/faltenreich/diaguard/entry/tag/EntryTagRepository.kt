package com.faltenreich.diaguard.entry.tag

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class EntryTagRepository(
    private val dao: EntryTagDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(entryTag: EntryTag.Intermediate): Long {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            entryId = entryTag.entry.id,
            tagId = entryTag.tag.id,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getLastId(): Long? {
        return dao.getLastId()
    }

    fun getByEntryId(entryId: Long): List<EntryTag.Local> {
        return dao.getByEntryId(entryId)
    }

    fun observeByTagId(tagId: Long): Flow<List<EntryTag.Local>> {
        return dao.observeByTagId(tagId)
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}