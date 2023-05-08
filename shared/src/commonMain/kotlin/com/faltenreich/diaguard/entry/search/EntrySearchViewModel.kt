package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class EntrySearchViewModel(
    query: String?,
    searchEntries: SearchEntriesUseCase = inject(),
    dispatcher: CoroutineDispatcher = inject(),
) : ViewModel() {

    private val state = MutableStateFlow<EntrySearchViewState>(EntrySearchViewState.Idle)
    val viewState = state.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            state
                .filterIsInstance<EntrySearchViewState.Loading>()
                .debounce(1.seconds)
                .distinctUntilChanged()
                .flatMapLatest { state -> searchEntries(query = state.query) }
                .onEach { entries -> state.value = EntrySearchViewState.Result(state.value.query, entries) }
                .collect()
        }
        query?.let(::onQueryChange)
    }

    fun onQueryChange(query: String) {
        state.value =
            if (query.isBlank()) EntrySearchViewState.Idle
            else EntrySearchViewState.Loading(query)
    }
}