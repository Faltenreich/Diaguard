package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface EntryDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        dateTime: DateTime,
        note: String?,
    )

    fun getLastId(): Long?

    fun getById(id: Long): Entry.Local?

    fun observeById(id: Long): Flow<Entry.Local?>

    fun getByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ): List<Entry.Local>

    fun getByQuery(query: String): List<Entry.Local>

    fun getAll(): Flow<List<Entry.Local>>

    fun countAll(): Flow<Long>

    fun update(
        id: Long,
        updatedAt: DateTime,
        dateTime: DateTime,
        note: String?,
    )

    fun deleteById(id: Long)
}