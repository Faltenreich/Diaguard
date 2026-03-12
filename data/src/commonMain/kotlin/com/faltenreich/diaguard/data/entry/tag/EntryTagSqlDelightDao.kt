package com.faltenreich.diaguard.data.entry.tag

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.faltenreich.diaguard.data.EntryTagQueries
import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

internal class EntryTagSqlDelightDao(
    private val dispatcher: CoroutineDispatcher,
    private val queries: EntryTagQueries,
    private val mapper: EntryTagSqlDelightMapper,
) : EntryTagDao {

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

    override fun observeByEntryId(entryId: Long): Flow<List<EntryTag.Local>> {
        return queries.getByEntry(entryId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun countByTagId(tagId: Long): Flow<Long> {
        return queries.countByTag(tagId).asFlow().mapToOne(dispatcher)
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}