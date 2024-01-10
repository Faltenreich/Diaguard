package com.faltenreich.diaguard.entry.form.tag

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.tag.EntryTag
import com.faltenreich.diaguard.tag.EntryTagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetEntryTagsUseCase(
    private val entryTagRepository: EntryTagRepository,
) {

    operator fun invoke(entry: Entry?): Flow<List<EntryTag>> {
        entry ?: return flowOf(emptyList())
        return entryTagRepository.observeByEntryId(entryId = entry.id)
    }
}