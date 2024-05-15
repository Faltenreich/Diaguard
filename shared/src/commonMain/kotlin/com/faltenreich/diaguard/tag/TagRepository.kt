package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class TagRepository(
    private val dao: TagDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(tag: Tag.Transfer): Long {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            name = tag.name,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): Tag.Persistent? {
        return dao.getById(id)
    }

    fun observeAll(): Flow<List<Tag.Persistent>> {
        return dao.observeAll()
    }

    fun observeByQuery(query: String): Flow<List<Tag.Persistent>> {
        return dao.observeByQuery(query)
    }

    fun getByName(name: String): Tag.Persistent? {
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