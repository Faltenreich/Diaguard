package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface EntryDao {

    fun getAll(): Flow<List<Entry>>

    fun create(createdAt: DateTime, dateTime: DateTime)

    fun getLastId(): Long?

    fun getById(id: Long): Entry?

    fun getByQuery(query: String): Flow<List<Entry>>

    fun update(entry: Entry)

    fun deleteById(id: Long)
}