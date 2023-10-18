package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.shared.database.sqldelight.EntryQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.EntrySqlDelightMapper
import com.faltenreich.diaguard.shared.datetime.DateTime
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

    override fun getById(id: Long): Flow<Entry?> {
        return queries.getById(id, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun getByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ): List<Entry> {
        return queries.getByDateRange(
            startDateTime = startDateTime.isoString,
            endDateTime = endDateTime.isoString,
            mapper::map,
        ).executeAsList()
    }

    override fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime
    ): Flow<List<Entry>> {
        return queries.getByDateRange(
            startDateTime = startDateTime.isoString,
            endDateTime = endDateTime.isoString,
            mapper::map,
        ).asFlow().mapToList(dispatcher)
    }

    override fun getByQuery(query: String): Flow<List<Entry>> {
        return queries.getByQuery(query, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun getAll(): Flow<List<Entry>> {
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
            created_at = createdAt.isoString,
            updated_at = updatedAt.isoString,
            date_time = dateTime.isoString,
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
            updated_at = updatedAt.isoString,
            date_time = dateTime.isoString,
            note = note,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}