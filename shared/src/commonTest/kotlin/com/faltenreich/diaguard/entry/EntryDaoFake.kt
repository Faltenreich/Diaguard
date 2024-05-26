package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class EntryDaoFake : EntryDao {

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        dateTime: DateTime,
        note: String?
    ) {
        TODO("Not yet implemented")
    }

    override fun getLastId(): Long? {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): Entry.Local? {
        TODO("Not yet implemented")
    }

    override fun observeById(id: Long): Flow<Entry.Local?> {
        TODO("Not yet implemented")
    }

    override fun getByDateRange(startDateTime: DateTime, endDateTime: DateTime): List<Entry.Local> {
        TODO("Not yet implemented")
    }

    override fun getByQuery(query: String): Flow<List<Entry.Local>> {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<List<Entry.Local>> {
        TODO("Not yet implemented")
    }

    override fun countAll(): Flow<Long> {
        TODO("Not yet implemented")
    }

    override fun update(id: Long, updatedAt: DateTime, dateTime: DateTime, note: String?) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }
}