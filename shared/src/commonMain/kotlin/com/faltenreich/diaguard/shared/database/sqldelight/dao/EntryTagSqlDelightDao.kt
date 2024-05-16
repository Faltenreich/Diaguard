package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.database.sqldelight.EntryTagQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.EntryTagSqlDelightMapper
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.EntryTag
import com.faltenreich.diaguard.tag.EntryTagDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class EntryTagSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: EntryTagSqlDelightMapper = inject(),
) : EntryTagDao, SqlDelightDao<EntryTagQueries> {

    override fun getQueries(api: SqlDelightApi): EntryTagQueries {
        return api.entryTagQueries
    }

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        entryId: Long,
        tagId: Long,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = updatedAt.isoString,
            entryId = entryId,
            tagId = tagId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getByEntryId(entryId: Long): List<EntryTag.Local> {
        return queries.getByEntry(entryId, mapper::map).executeAsList()
    }

    override fun observeByTagId(tagId: Long): Flow<List<EntryTag.Local>> {
        return queries.getByTag(tagId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun countByTagId(tagId: Long): Flow<Long> {
        return queries.countByTag(tagId).asFlow().mapToOne(dispatcher)
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}