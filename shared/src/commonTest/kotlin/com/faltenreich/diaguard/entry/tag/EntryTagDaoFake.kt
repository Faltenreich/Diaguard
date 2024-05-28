package com.faltenreich.diaguard.entry.tag

import androidx.compose.runtime.mutableStateListOf
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.TagDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EntryTagDaoFake(
    private val entryDao: EntryDao = inject(),
    private val tagDao: TagDao = inject(),
) : EntryTagDao {

    private val cache = mutableStateListOf<EntryTag.Local>()

    override fun create(createdAt: DateTime, updatedAt: DateTime, entryId: Long, tagId: Long) {
        cache += EntryTag.Local(
            id = cache.size.toLong(),
            createdAt = createdAt,
            updatedAt = updatedAt,
            entry = entryDao.getById(entryId)!!,
            tag = tagDao.getById(entryId)!!,
        )
    }

    override fun getLastId(): Long? {
        return cache.lastOrNull()?.id
    }

    override fun getByEntryId(entryId: Long): List<EntryTag.Local> {
        return cache.filter { it.entry.id == entryId }
    }

    override fun observeByTagId(tagId: Long): Flow<List<EntryTag.Local>> {
        return flowOf(cache.filter { it.tag.id == tagId })
    }

    override fun countByTagId(tagId: Long): Flow<Long> {
        return flowOf(cache.count { it.tag.id == tagId }.toLong())
    }

    override fun deleteById(id: Long) {
        val entry = cache.firstOrNull { it.id == id } ?: return
        cache.remove(entry)
    }
}