package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class EntryRepository(
    private val dao: EntryDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(entry: Entry.Legacy): Long {
        dao.create(
            createdAt = entry.createdAt,
            updatedAt = entry.updatedAt,
            dateTime = entry.dateTime,
            note = entry.note,
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(entry: Entry.User): Long {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            dateTime = entry.dateTime,
            note = entry.note,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): Entry.Local? {
        return dao.getById(id)
    }

    fun getByDateRange(startDateTime: DateTime, endDateTime: DateTime): List<Entry.Local> {
        return dao.getByDateRange(startDateTime, endDateTime)
    }

    fun getByQuery(query: String): List<Entry.Local> {
        return dao.getByQuery(query)
    }

    fun getAll(): Flow<List<Entry.Local>> {
        return dao.getAll()
    }

    fun countAll(): Flow<Long> {
        return dao.countAll()
    }

    fun update(entry: Entry.Local) = with(entry) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            dateTime = dateTime,
            note = note,
        )
    }

    fun delete(entry: Entry.Local) {
        dao.deleteById(entry.id)
    }
}