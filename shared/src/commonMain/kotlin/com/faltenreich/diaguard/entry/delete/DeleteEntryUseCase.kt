package com.faltenreich.diaguard.entry.delete

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository

class DeleteEntryUseCase(
    private val entryRepository: EntryRepository,
) {

    operator fun invoke(entry: Entry) {
        entryRepository.deleteById(entry.id)
    }
}