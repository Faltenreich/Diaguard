package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EntryListViewModel(
    private val deleteEntry: DeleteEntryUseCase = inject(),
) : ViewModel<Unit, EntryListIntent>() {

    // TODO: Introduce state
    override val state: Flow<Unit>
        get() = flowOf(Unit)

    override fun onIntent(intent: EntryListIntent) {
        when (intent) {
            is EntryListIntent.Delete -> deleteEntry(intent.entry.id)
        }
    }
}