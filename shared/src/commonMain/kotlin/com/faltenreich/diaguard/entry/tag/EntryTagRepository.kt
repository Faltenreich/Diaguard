package com.faltenreich.diaguard.entry.tag

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class EntryTagRepository(
    private val dao: EntryTagDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(entryTag: EntryTag.Legacy): Long = with(entryTag){
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            entryId = entry.id,
            tagId = tag.id,
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(entryTag: EntryTag.Intermediate): Long = with(entryTag) {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            entryId = entry.id,
            tagId = tag.id,
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