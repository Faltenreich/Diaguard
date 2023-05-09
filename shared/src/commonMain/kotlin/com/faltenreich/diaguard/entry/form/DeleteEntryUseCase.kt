package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.shared.di.inject

class DeleteEntryUseCase(
    private val entryRepository: EntryRepository = inject(),
) {

    operator fun invoke(id: Long) {
        entryRepository.deleteById(id)
    }
}