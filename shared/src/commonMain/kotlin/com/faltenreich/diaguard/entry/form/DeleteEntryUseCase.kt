package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.EntryRepository

class DeleteEntryUseCase(
    private val entryRepository: EntryRepository,
) {

    operator fun invoke(id: Long) {
        entryRepository.deleteById(id)
    }
}