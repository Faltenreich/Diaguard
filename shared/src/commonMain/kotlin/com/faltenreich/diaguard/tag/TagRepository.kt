package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class TagRepository(
    private val dao: TagDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        name: String,
    ): Tag {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            name = name,
        )
        val id = checkNotNull(dao.getLastId())
        return Tag(
            id = id,
            createdAt = now,
            updatedAt = now,
            name = name,
        )
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
        name: String,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}