package com.faltenreich.diaguard.tag.delete

import com.faltenreich.diaguard.tag.EntryTagRepository
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.flow.Flow

class CountEntriesByTagUseCase(
    private val repository: EntryTagRepository,
) {

    operator fun invoke(tag: Tag): Flow<Long> {
        return repository.countByTagId(tag.id)
    }
}