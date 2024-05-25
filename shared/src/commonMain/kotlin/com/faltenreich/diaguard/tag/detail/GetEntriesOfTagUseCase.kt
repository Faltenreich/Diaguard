package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.flow.Flow

class GetEntriesOfTagUseCase(
    private val repository: EntryTagRepository,
) {

    operator fun invoke(tag: Tag.Local): Flow<List<EntryTag.Local>> {
        // FIXME: Misses MeasurementValues of Entry
        return repository.observeByTagId(tag.id)
    }
}