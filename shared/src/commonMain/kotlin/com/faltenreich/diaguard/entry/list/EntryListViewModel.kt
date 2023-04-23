package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
class EntryListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val entryRepository: EntryRepository = inject(),
) : ViewModel() {

    private val state = MutableStateFlow(EntryListViewState())
    val viewState = state.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            val entries = entryRepository.getAll()
            state.value = state.value.copy(entries = entries)
        }
    }
}