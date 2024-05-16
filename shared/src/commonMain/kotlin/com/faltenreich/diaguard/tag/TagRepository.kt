package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class TagRepository(
    private val dao: TagDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(tag: Tag.User): Long {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            name = tag.name,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): Tag.Local? {
        return dao.getById(id)
    }

    fun observeAll(): Flow<List<Tag.Local>> {
        return dao.observeAll()
    }

    fun observeByQuery(query: String): Flow<List<Tag.Local>> {
        return dao.observeByQuery(query)
    }

    fun getByName(name: String): Tag.Local? {
        return dao.getByName(name)
    }

    fun update(tag: Tag.Local) = with(tag) {
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