package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.shared.database.sqldelight.EntryTagQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.EntryTagSqlDelightMapper
import com.faltenreich.diaguard.shared.datetime.DateTime
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
            created_at = createdAt.isoString,
            updated_at = updatedAt.isoString,
            entry_id = entryId,
            tag_id = tagId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun observeByEntryId(entryId: Long): Flow<List<EntryTag>> {
        return queries.getByEntry(entryId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeByTagId(tagId: Long): Flow<List<EntryTag>> {
        return queries.getByTag(tagId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}