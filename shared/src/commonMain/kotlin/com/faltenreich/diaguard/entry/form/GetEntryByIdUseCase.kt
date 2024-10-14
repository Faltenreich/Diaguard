package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository

class GetEntryByIdUseCase(
    private val entryRepository: EntryRepository,
) {

    operator fun invoke(id: Long): Entry.Local? {
        return entryRepository.getById(id)
    }
}