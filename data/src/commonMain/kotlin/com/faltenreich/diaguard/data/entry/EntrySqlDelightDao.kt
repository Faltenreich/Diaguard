package com.faltenreich.diaguard.data.entry

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.data.SqlDelightDao
import com.faltenreich.diaguard.data.EntryQueries
import com.faltenreich.diaguard.data.SqlDelightApi
import com.faltenreich.diaguard.view.paging.PagingPage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

internal class EntrySqlDelightDao(
    private val dispatcher: CoroutineDispatcher,
    private val mapper: EntrySqlDelightMapper,
) : EntryDao, SqlDelightDao<EntryQueries> {

    override fun getQueries(api: SqlDelightApi): EntryQueries {
        return api.entryQueries
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getById(id: Long): Entry.Local? {
        return queries.getById(id, mapper::map).executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<Entry.Local?> {
        return queries.getById(id, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun getByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ): List<Entry.Local> {
        return queries.getByDateRange(
            startDateTime = startDateTime.isoString,
            endDateTime = endDateTime.isoString,
            mapper::map,
        ).executeAsList()
    }

    override fun getByQuery(query: String, page: PagingPage): List<Entry.Local> {
        return queries.getByQuery(
            query = query,
            offset = page.page.toLong() * page.pageSize.toLong(),
            limit = page.pageSize.toLong(),
            mapper = mapper::map,
        ).executeAsList()
    }

    override fun getByTagId(tagId: Long, page: PagingPage): List<Entry.Local> {
        return queries.getByTag(
            tagId = tagId,
            offset = page.page.toLong() * page.pageSize.toLong(),
            limit = page.pageSize.toLong(),
            mapper = mapper::map,
        ).executeAsList()
    }

    override fun getAll(): Flow<List<Entry.Local>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        dateTime: DateTime,
        note: String?,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = updatedAt.isoString,
            dateTime = dateTime.isoString,
            note = note,
        )
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        dateTime: DateTime,
        note: String?,
    ) {
        queries.update(
            updatedAt = updatedAt.isoString,
            dateTime = dateTime.isoString,
            note = note,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}