package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.entry.Entry

class CreateEntryTagsUseCase(private val repository: EntryTagRepository) {

    operator fun invoke(
        tags: List<Tag>,
        entry: Entry.Local,
    ) {
        val entryTagsFromBefore = repository.getByEntryId(entry.id)
        createMissingEntryTags(tags, entry, entryTagsFromBefore)
        deleteObsoleteEntryTags(tags, entryTagsFromBefore)
    }

    private fun createMissingEntryTags(
        tags: List<Tag>,
        entry: Entry.Local,
        entryTagsFromBefore: List<EntryTag>,
    ) {
        val missingTags = tags.filterIsInstance<Tag.Local>().filter { tag ->
            entryTagsFromBefore.filterIsInstance<EntryTag.Local>().none { entryTag ->
                entryTag.tag.id == tag.id
            }
        }
        missingTags.forEach { tag ->
            val entryTag = EntryTag.Intermediate(entry, tag)
            repository.create(entryTag)
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
            repository.deleteById(entryTag.id)
        }
    }
}