package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.deep
import kotlinx.coroutines.flow.Flow

class SearchEntriesUseCase(
    private val entryRepository: EntryRepository,
) {

    operator fun invoke(query: String): Flow<List<Entry>> {
        return entryRepository.search(query).deep()
    }
}