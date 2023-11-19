package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class EntryRepository(
    private val dao: EntryDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        dateTime: DateTime,
        note: String?,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            dateTime = dateTime,
            note = note,
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(dateTime: DateTime): Long {
        val now = dateTimeFactory.now()
        return create(
            createdAt = now,
            updatedAt = now,
            dateTime = dateTime,
            note = null,
        )
    }

    fun getByDateRange(startDateTime: DateTime, endDateTime: DateTime): List<Entry> {
        return dao.getByDateRange(startDateTime, endDateTime)
    }

    fun observeByDateRange(startDateTime: DateTime, endDateTime: DateTime): Flow<List<Entry>> {
        return dao.observeByDateRange(startDateTime, endDateTime)
    }

    fun getAll(): Flow<List<Entry>> {
        return dao.getAll()
    }

    fun countAll(): Flow<Long> {
        return dao.countAll()
    }

    fun update(
        id: Long,
        dateTime: DateTime,
        note: String?,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            dateTime = dateTime,
            note = note,
        )
    }

    fun update(entry: Entry) {
        update(
            id = entry.id,
            dateTime = entry.dateTime,
            note = entry.note,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }

    fun search(query: String): Flow<List<Entry>> {
        return dao.getByQuery(query)
    }
}