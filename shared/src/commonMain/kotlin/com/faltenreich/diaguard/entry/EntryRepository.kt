package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class EntryRepository(
    private val dao: EntryDao,
) {

    fun getAll(): Flow<List<Entry>> {
        return dao.getAll()
    }

    fun create(dateTime: DateTime): Long {
        dao.create(createdAt = DateTime.now(), dateTime = dateTime)
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun update(
        id: Long,
        dateTime: DateTime,
        note: String?,
    ) {
        dao.update(
            id = id,
            updatedAt = DateTime.now(),
            dateTime = dateTime,
            note = note,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }

    fun search(query: String): Flow<List<Entry>> {
        return dao.getByQuery(query)
    }
}