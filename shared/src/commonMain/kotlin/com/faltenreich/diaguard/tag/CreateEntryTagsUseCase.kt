package com.faltenreich.diaguard.tag

class CreateEntryTagsUseCase(
    private val entryTagRepository: EntryTagRepository,
) {

    operator fun invoke(
        tags: List<Tag>,
        entryId: Long,
    ) {
        val entryTagsFromBefore = entryTagRepository.getByEntryId(entryId)
        createMissingEntryTags(tags, entryId, entryTagsFromBefore)
        deleteObsoleteEntryTags(tags, entryTagsFromBefore)
    }

    private fun createMissingEntryTags(
        tags: List<Tag>,
        entryId: Long,
        entryTagsFromBefore: List<EntryTag>,
    ) {
        val missingTags = tags.filterIsInstance<Tag.Persistent>().filter { tag ->
            entryTagsFromBefore.none { entryTag ->
                entryTag.tagId == tag.id
            }
        }
        missingTags.forEach { tag ->
            entryTagRepository.create(
                entryId = entryId,
                tagId = tag.id,
            )
        }
    }

    private fun deleteObsoleteEntryTags(
        tags: List<Tag>,
        entryTagsFromBefore: List<EntryTag>,
    ) {
        val obsoleteEntryTags = entryTagsFromBefore.filter { entryTag ->
            tags.filterIsInstance<Tag.Persistent>().none { tag ->
                entryTag.tagId == tag.id
            }
        }
        obsoleteEntryTags.forEach { entryTag ->
            entryTagRepository.deleteById(entryTag.id)
        }
    }
}