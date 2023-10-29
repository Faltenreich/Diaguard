package com.faltenreich.diaguard.entry.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.seconds

class EntrySearchViewModel(
    query: String = "",
    searchEntries: SearchEntriesUseCase = inject(),
) : ViewModel() {

    private val state = MutableStateFlow<EntrySearchViewState>(EntrySearchViewState.Idle)
    val viewState = state.asStateFlow()

    var query: String by mutableStateOf("")

    init {
        snapshotFlow { this.query }
            .debounce(1.seconds)
            .distinctUntilChanged()
            .onEach { state.value = EntrySearchViewState.Loading }
            .flatMapLatest { searchEntries(it) }
            .onEach {
                state.value =
                    if (this.query.isBlank()) EntrySearchViewState.Idle
                    else EntrySearchViewState.Result(it)
            }
            .launchIn(viewModelScope)

        if (query.isNotBlank()) {
            this.query = query
        }
    }
}