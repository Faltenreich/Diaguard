package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject

class EntryListViewModel(
    private val entryRepository: EntryRepository = inject(),
) : ViewModel() {

    fun delete(entry: Entry) {
        entryRepository.delete(entry)
    }
}