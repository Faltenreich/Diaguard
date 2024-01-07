package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.tag.EntryTag
import com.faltenreich.diaguard.tag.EntryTagRepository
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.flow.Flow

class GetEntriesOfTagUseCase(
    private val repository: EntryTagRepository,
) {

    operator fun invoke(tag: Tag): Flow<List<EntryTag>> {
        return repository.observeByTagId(tag.id)
    }
}