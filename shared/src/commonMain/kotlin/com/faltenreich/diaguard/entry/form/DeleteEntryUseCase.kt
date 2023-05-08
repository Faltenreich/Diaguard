package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.shared.di.inject

class DeleteEntryUseCase(
    private val entryRepository: EntryRepository = inject(),
) {

    operator fun invoke(entry: Entry) {
        entryRepository.delete(entry)
    }
}