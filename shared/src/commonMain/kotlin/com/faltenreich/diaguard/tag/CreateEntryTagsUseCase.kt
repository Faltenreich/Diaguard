package com.faltenreich.diaguard.tag

class CreateEntryTagsUseCase(
    private val entryTagRepository: EntryTagRepository,
) {

    operator fun invoke(
        tags: List<Tag>,
        entryId: Long,
    ) {
        TODO()
    }
}