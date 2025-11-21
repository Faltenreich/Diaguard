package com.faltenreich.diaguard.entry.tag

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.entry.tag.EntryTag
import com.faltenreich.diaguard.data.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.data.tag.TagRepository

class StoreEntryTagsUseCase(
    private val tagRepository: TagRepository,
    private val entryTagRepository: EntryTagRepository,
) {

    operator fun invoke(
        tags: Collection<Tag>,
        entry: Entry.Local,
    ) {
        val entryTagsFromBefore = entryTagRepository.getByEntryId(entry.id)
        createMissingEntryTags(tags, entry, entryTagsFromBefore)
        deleteObsoleteEntryTags(tags, entryTagsFromBefore)
    }

    private fun createMissingEntryTags(
        tags: Collection<Tag>,
        entry: Entry.Local,
        entryTagsFromBefore: Collection<EntryTag>,
    ) {
        val tagsByLocal = tags.filterIsInstance<Tag.Local>()
        val tagsByUser = tags.filterIsInstance<Tag.User>()
            // Filter out redundant tags
            .filterNot { user -> tagsByLocal.any { local -> local.name == user.name } }
            .map { tag -> checkNotNull(tagRepository.create(tag).let(tagRepository::getById)) }
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
        tags: Collection<Tag>,
        entryTagsFromBefore: Collection<EntryTag>,
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