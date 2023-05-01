package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LogViewModel(
    private val entryRepository: EntryRepository,
) : ViewModel() {

    private val entries = entryRepository.getAll()
    private val state = entries.map(::LogViewState)
    val viewState = state.stateIn(viewModelScope, SharingStarted.Lazily, LogViewState())

    fun delete(entry: Entry) {
        entryRepository.delete(entry)
    }
}