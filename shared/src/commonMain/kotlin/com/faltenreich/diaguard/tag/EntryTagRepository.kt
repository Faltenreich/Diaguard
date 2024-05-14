package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class EntryTagRepository(
    private val dao: EntryTagDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        entryId: Long,
        tagId: Long,
    ): EntryTag {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            entryId = entryId,
            tagId = tagId,
        )
        val id = checkNotNull(dao.getLastId())
        return EntryTag(
            id = id,
            createdAt = now,
            updatedAt = now,
            entryId = entryId,
            tagId = tagId,
        )
    }

    fun getLastId(): Long? {
        return dao.getLastId()
    }

    fun getByEntryId(entryId: Long): List<EntryTag> {
        return dao.getByEntryId(entryId)
    }

    fun observeByTagId(tagId: Long): Flow<List<EntryTag>> {
        return dao.observeByTagId(tagId)
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