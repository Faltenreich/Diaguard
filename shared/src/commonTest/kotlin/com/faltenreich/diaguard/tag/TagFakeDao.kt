package com.faltenreich.diaguard.tag

import androidx.compose.runtime.mutableStateListOf
import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

open class TagFakeDao : TagDao {

    private val cache = mutableStateListOf<Tag.Local>()

    override fun create(createdAt: DateTime, updatedAt: DateTime, name: String) {
        cache += Tag.Local(
            id = cache.size.toLong(),
            createdAt = createdAt,
            updatedAt = updatedAt,
            name = name,
        )
    }

    override fun getLastId(): Long? {
        return cache.lastOrNull()?.id
    }

    override fun getById(id: Long): Tag.Local? {
        return cache.firstOrNull { it.id == id }
    }

    override fun observeAll(): Flow<List<Tag.Local>> {
        return flowOf(cache)
    }

    override fun observeByQuery(query: String): Flow<List<Tag.Local>> {
        return flowOf(cache.filter { it.name.contains(query, ignoreCase = true) })
    }

    override fun getByName(name: String): Tag.Local? {
        return cache.firstOrNull { it.name == name }
    }

    override fun update(id: Long, updatedAt: DateTime, name: String) {
        val entity = cache.firstOrNull { it.id == id } ?: return
        val index = cache.indexOf(entity)
        cache[index] = entity.copy(
            updatedAt = updatedAt,
            name = name,
        )
    }

    override fun deleteById(id: Long) {
        val entry = cache.firstOrNull { it.id == id } ?: return
        cache.remove(entry)
    }
}