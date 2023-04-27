package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface EntryDao {

    fun getAll(): Flow<List<Entry>>

    fun create(dateTime: DateTime)

    fun getLastId(): Long?

    fun getById(id: Long): Entry?

    fun update(entry: Entry)

    fun delete(entry: Entry)
}