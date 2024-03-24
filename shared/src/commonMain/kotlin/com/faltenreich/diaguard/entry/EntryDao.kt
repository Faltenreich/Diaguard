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

    fun getById(id: Long): Flow<Entry?>

    fun getByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ): List<Entry>

    fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ): Flow<List<Entry>>

    fun getByQuery(query: String): Flow<List<Entry>>

    fun getAll(): Flow<List<Entry>>

    fun countAll(): Flow<Long>

    fun update(
        id: Long,
        updatedAt: DateTime,
        dateTime: DateTime,
        note: String?,
    )

    fun deleteById(id: Long)
}