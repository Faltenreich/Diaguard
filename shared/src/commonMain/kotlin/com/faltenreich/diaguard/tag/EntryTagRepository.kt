package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class EntryTagRepository(
    private val dao: EntryTagDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(entryTag: EntryTag.Transfer): Long {
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

    fun getByEntryId(entryId: Long): List<EntryTag.Persistent> {
        return dao.getByEntryId(entryId)
    }

    fun observeByTagId(tagId: Long): Flow<List<EntryTag.Persistent>> {
        return dao.observeByTagId(tagId)
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}