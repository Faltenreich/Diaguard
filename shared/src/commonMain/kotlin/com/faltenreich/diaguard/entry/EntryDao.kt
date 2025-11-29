package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.data.PagingPage
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

    fun getByQuery(query: String, page: PagingPage): List<Entry.Local>

    fun getByTagId(tagId: Long, page: PagingPage): List<Entry.Local>

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