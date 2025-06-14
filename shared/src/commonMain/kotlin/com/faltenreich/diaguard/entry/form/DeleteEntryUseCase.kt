package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository

class DeleteEntryUseCase(
    private val entryRepository: EntryRepository,
) {

    operator fun invoke(entry: Entry.Local) {
        entryRepository.delete(entry)
    }
}