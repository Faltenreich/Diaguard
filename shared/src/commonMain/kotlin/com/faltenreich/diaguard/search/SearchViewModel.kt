package com.faltenreich.diaguard.search

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SearchViewModel(
    private val searchUseCase: SearchUseCase,
    dispatcher: CoroutineDispatcher,
): ViewModel() {

    private val state = MutableStateFlow<SearchState>(SearchState.Idle)
    val uiState = state.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            state
                .filterIsInstance<SearchState.Loading>()
                .debounce(1.seconds)
                .distinctUntilChanged()
                .flatMapLatest { state -> searchUseCase(state.query) }
                .onEach { words -> state.value = SearchState.Result(state.value.query, words) }
                .collect()
        }
    }

    fun onQueryChanged(query: String) {
        state.value = if (query.isBlank()) SearchState.Idle else SearchState.Loading(query)
    }
}