package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class EntryTagRepository(
    private val dao: EntryTagDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        entryId: Long,
        tagId: Long,
    ): Long {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            entryId = entryId,
            tagId = tagId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getLastId(): Long? {
        return dao.getLastId()
    }

    fun getByEntryId(entryId: Long): List<EntryTag> {
        return dao.getByEntryId(entryId)
    }

    fun observeByEntryId(entryId: Long): Flow<List<EntryTag>> {
        return dao.observeByEntryId(entryId)
    }

    fun observeByTagId(tagId: Long): Flow<List<EntryTag>> {
        return dao.observeByTagId(tagId)
    }

    fun countByTagId(tagId: Long): Flow<Long> {
        return dao.countByTagId(tagId)
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}

fun List<EntryTag>.deep(
    entry: Entry,
    tagRepository: TagRepository = inject(),
): List<EntryTag> {
    return map { entryTag ->
        entryTag.entry = entry
        entryTag.tag = checkNotNull(tagRepository.getById(entryTag.tagId))
        entryTag
    }
}