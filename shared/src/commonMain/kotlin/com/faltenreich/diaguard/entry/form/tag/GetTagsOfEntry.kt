package com.faltenreich.diaguard.entry.form.tag

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.entry.tag.EntryTag
import com.faltenreich.diaguard.data.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.data.tag.Tag
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetTagsOfEntry(
    private val dispatcher: CoroutineDispatcher,
    private val entryTagRepository: EntryTagRepository,
) {

    suspend operator fun invoke(entry: Entry.Local): Flow<List<Tag>> = withContext(dispatcher) {
        entryTagRepository.observeByEntryId(entry.id)
            .map { entryTags ->
                entryTags.map(EntryTag::tag)
            }
    }
}