package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class EntryRepository(
    private val dao: EntryDao,
) {

    fun getAll(): Flow<List<Entry>> {
        return dao.getAll()
    }

    fun create(): Entry {
        val now = DateTime.now()
        dao.create(createdAt = now, dateTime = now)
        val id = dao.getLastId() ?: throw IllegalStateException("No entry found")
        return dao.getById(id) ?: throw IllegalStateException("No entry found")
    }

    fun update(entry: Entry) {
        dao.update(entry.copy(updatedAt = DateTime.now()))
    }

    fun delete(entry: Entry) {
        dao.delete(entry)
    }

    fun search(query: String): Flow<List<Entry>> {
        return dao.getByQuery(query)
    }
}