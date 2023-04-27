package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Single

@Single
class EntryFormViewModel(
    entry: Entry?,
    private val entryRepository: EntryRepository = inject(),
) : ViewModel() {

    private val state = MutableStateFlow(EntryFormViewState(entry ?: entryRepository.create()))
    val viewState = state.asStateFlow()

    fun setNote(note: String) {
        state.value = state.value.copy(entry = state.value.entry.copy(note = note))
    }

    fun submit() {
        entryRepository.update(state.value.entry)
    }
}