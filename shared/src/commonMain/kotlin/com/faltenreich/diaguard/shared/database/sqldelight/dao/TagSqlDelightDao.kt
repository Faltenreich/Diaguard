package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.TagQueries
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.TagSqlDelightMapper
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class TagSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: TagSqlDelightMapper = inject(),
) : TagDao, SqlDelightDao<TagQueries> {

    override fun getQueries(api: SqlDelightApi): TagQueries {
        return api.tagQueries
    }

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        name: String,
    ) {
        queries.create(
            created_at = createdAt.isoString,
            updated_at = updatedAt.isoString,
            name = name,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun observeAll(): Flow<List<Tag>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeByQuery(query: String): Flow<List<Tag>> {
        return queries.getByQuery(query, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun getByName(name: String): Tag? {
        return queries.getByName(name, mapper::map).executeAsOneOrNull()
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
    ) {
        queries.update(
            updated_at = updatedAt.isoString,
            name = name,
            id = id,
        )
    }


    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}