package com.faltenreich.diaguard.entry.form.tag

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.tag.EntryTag
import com.faltenreich.diaguard.tag.EntryTagRepository
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetTagsOfEntry(
    private val dispatcher: CoroutineDispatcher,
    private val entryTagRepository: EntryTagRepository,
) {

    suspend operator fun invoke(entry: Entry.Local?): List<Tag> = withContext(dispatcher) {
        entry ?: return@withContext emptyList()
        entryTagRepository.getByEntryId(entryId = entry.id).map(EntryTag::tag)
    }
}