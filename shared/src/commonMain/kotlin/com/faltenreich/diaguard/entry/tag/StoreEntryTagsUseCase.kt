package com.faltenreich.diaguard.entry.tag

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository

class StoreEntryTagsUseCase(
    private val tagRepository: TagRepository,
    private val entryTagRepository: EntryTagRepository,
) {

    operator fun invoke(
        tags: List<Tag>,
        entry: Entry.Local,
    ) {
        val entryTagsFromBefore = entryTagRepository.getByEntryId(entry.id)
        createMissingEntryTags(tags, entry, entryTagsFromBefore)
        deleteObsoleteEntryTags(tags, entryTagsFromBefore)
    }

    private fun createMissingEntryTags(
        tags: List<Tag>,
        entry: Entry.Local,
        entryTagsFromBefore: List<EntryTag>,
    ) {
        val tagsByLocal = tags.filterIsInstance<Tag.Local>()
        val tagsByUser = tags.filterIsInstance<Tag.User>().map { tag ->
            tagsByLocal.firstOrNull { it.name == tag.name }
                ?: tagRepository.getByName(tag.name)
                ?: checkNotNull(tagRepository.create(tag).let(tagRepository::getById))
        }
        val missingTags = (tagsByLocal + tagsByUser).filter { tag ->
            entryTagsFromBefore.filterIsInstance<EntryTag.Local>().none { entryTag ->
                entryTag.tag.id == tag.id
            }
        }
        missingTags.forEach { tag ->
            val entryTag = EntryTag.Intermediate(entry, tag)
            entryTagRepository.create(entryTag)
        }
    }

    private fun deleteObsoleteEntryTags(
        tags: List<Tag>,
        entryTagsFromBefore: List<EntryTag>,
    ) {
        val obsoleteEntryTags = entryTagsFromBefore.filterIsInstance<EntryTag.Local>().filter { entryTag ->
            tags.filterIsInstance<Tag.Local>().none { tag ->
                entryTag.tag.id == tag.id
            }
        }
        obsoleteEntryTags.forEach { entryTag ->
            entryTagRepository.deleteById(entryTag.id)
        }
    }
}