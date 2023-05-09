package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject

class EntryListViewModel(
    private val deleteEntry: DeleteEntryUseCase = inject(),
) : ViewModel() {

    fun delete(entry: Entry) {
        deleteEntry(entry.id)
    }
}