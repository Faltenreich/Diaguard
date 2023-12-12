package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class EntryListViewModel(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
) : ViewModel {

    fun delete(entry: Entry) = scope.launch(dispatcher) {
        deleteEntry(entry.id)
    }
}