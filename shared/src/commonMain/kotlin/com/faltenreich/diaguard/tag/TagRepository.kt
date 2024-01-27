package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class TagRepository(
    private val dao: TagDao,
) {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        name: String,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            name = name,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): Tag? {
        return dao.getById(id)
    }

    fun observeAll(): Flow<List<Tag>> {
        return dao.observeAll()
    }

    fun observeByQuery(query: String): Flow<List<Tag>> {
        return dao.observeByQuery(query)
    }

    fun getByName(name: String): Tag? {
        return dao.getByName(name)
    }

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
    ) {
        dao.update(
            id = id,
            updatedAt = updatedAt,
            name = name,
        )
    }

    fun update(tag: Tag) = with(tag) {
        update(
            id = id,
            updatedAt = updatedAt,
            name = name,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}