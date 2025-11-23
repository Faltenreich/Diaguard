package com.faltenreich.diaguard.data.tag

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.data.TagQueries
import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

internal class TagSqlDelightDao(
    private val dispatcher: CoroutineDispatcher,
    private val queries: TagQueries,
    private val mapper: TagSqlDelightMapper,
) : TagDao {

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        name: String,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = updatedAt.isoString,
            name = name,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getById(id: Long): Tag.Local? {
        return queries.getById(id, mapper::map).executeAsOneOrNull()
    }

    override fun observeAll(): Flow<List<Tag.Local>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeByQuery(query: String): Flow<List<Tag.Local>> {
        return queries.getByQuery(query, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun getByName(name: String): Tag.Local? {
        return queries.getByName(name, mapper::map).executeAsOneOrNull()
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
    ) {
        queries.update(
            updatedAt = updatedAt.isoString,
            name = name,
            id = id,
        )
    }


    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}