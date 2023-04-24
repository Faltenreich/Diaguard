package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Single

@Single
class EntryListViewModel(
    private val entryRepository: EntryRepository = inject(),
) : ViewModel() {

    private val entries = entryRepository.getAll()
    private val state = entries.map(::EntryListViewState)
    val viewState = state.stateIn(viewModelScope, SharingStarted.Lazily, EntryListViewState())

    fun delete(entry: Entry) {
        entryRepository.delete(entry)
    }
}