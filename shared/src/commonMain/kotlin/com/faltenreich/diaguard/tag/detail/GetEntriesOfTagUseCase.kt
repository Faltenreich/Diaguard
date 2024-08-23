package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetEntriesOfTagUseCase(
    private val repository: EntryTagRepository,
) {

    operator fun invoke(tag: Tag.Local): Flow<List<EntryTag.Localized>> {
        // FIXME: Misses MeasurementValues of Entry
        return repository.observeByTagId(tag.id).map {
            TODO()
        }
    }
}