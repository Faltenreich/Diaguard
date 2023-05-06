package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import kotlinx.coroutines.flow.Flow

class EntrySearchUseCase(
    private val entryRepository: EntryRepository,
) {

    operator fun invoke(query: String): Flow<List<Entry>> {
        return entryRepository.search(query)
    }
}