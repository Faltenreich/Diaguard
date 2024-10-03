package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.database.sqldelight.EntryQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.EntrySqlDelightMapper
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class EntrySqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: EntrySqlDelightMapper = inject(),
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

    override fun getAll(): Flow<List<Entry.Local>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun countAll(): Flow<Long> {
        return queries.countAll().asFlow().mapToOne(dispatcher)
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