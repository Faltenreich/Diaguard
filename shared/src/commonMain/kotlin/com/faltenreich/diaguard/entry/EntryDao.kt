package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface EntryDao {

    fun create(createdAt: DateTime, dateTime: DateTime)

    fun getLastId(): Long?

    fun getById(id: Long): Entry?

    fun getByQuery(query: String): Flow<List<Entry>>

    fun getAll(): Flow<List<Entry>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        dateTime: DateTime,
        note: String?,
    )

    fun deleteById(id: Long)
}