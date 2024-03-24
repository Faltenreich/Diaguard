package com.faltenreich.diaguard.entry.delete

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EntryDeleteViewModel(
    private val entry: Entry?,
    private val closeModal: CloseModalUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
) : ViewModel<EntryDeleteState, EntryDeleteIntent>() {

    override val state: Flow<EntryDeleteState> = flowOf(EntryDeleteState(entry))

    override fun onIntent(intent: EntryDeleteIntent) {
        when (intent) {
            is EntryDeleteIntent.Close -> closeModal()
            is EntryDeleteIntent.Confirm -> {
                entry?.let(deleteEntry::invoke)
                closeModal()
            }
        }
    }
}