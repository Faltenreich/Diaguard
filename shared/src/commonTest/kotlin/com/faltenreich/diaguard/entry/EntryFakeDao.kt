package com.faltenreich.diaguard.entry

import androidx.compose.runtime.mutableStateListOf
import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow

open class EntryFakeDao : EntryDao {

    private val cache = mutableStateListOf<Entry.Local>()

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        dateTime: DateTime,
        note: String?
    ) {
        cache += Entry.Local(
            id = cache.size.toLong(),
            createdAt = createdAt,
            updatedAt = updatedAt,
            dateTime = dateTime,
            note = note,
        )
    }

    override fun getLastId(): Long? {
        return cache.lastOrNull()?.id
    }

    override fun getById(id: Long): Entry.Local? {
        return cache.firstOrNull { it.id == id }
    }

    override fun observeById(id: Long): Flow<Entry.Local?> {
        return cache.asFlow().filter { it.id == id }
    }

    override fun getByDateRange(startDateTime: DateTime, endDateTime: DateTime): List<Entry.Local> {
        return cache.filter { it.dateTime > startDateTime && it.dateTime < endDateTime }
    }

    override fun getByQuery(query: String): Flow<List<Entry.Local>> {
        return flow { cache.filter { it.note == query } }
    }

    override fun getAll(): Flow<List<Entry.Local>> {
        return flow { cache }
    }

    override fun countAll(): Flow<Long> {
        return flow { cache.size }
    }

    override fun update(id: Long, updatedAt: DateTime, dateTime: DateTime, note: String?) {
        val entity = cache.firstOrNull { it.id == id } ?: return
        val index = cache.indexOf(entity)
        cache[index] = entity.copy(
            updatedAt = updatedAt,
            dateTime = dateTime,
            note = note,
        )
    }

    override fun deleteById(id: Long) {
        val entry = cache.firstOrNull { it.id == id } ?: return
        cache.remove(entry)
    }
}