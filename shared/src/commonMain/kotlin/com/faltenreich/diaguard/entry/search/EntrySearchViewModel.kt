package com.faltenreich.diaguard.entry.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.list.EntryListPagingSource
import com.faltenreich.diaguard.navigation.screen.NavigateToScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.seconds

class EntrySearchViewModel(
    query: String = "",
    searchEntries: SearchEntriesUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
) : ViewModel<EntrySearchState, EntrySearchIntent, Unit>() {

    var query: String by mutableStateOf("")

    private lateinit var pagingSource: EntryListPagingSource
    val pagingData = Pager(
        config = EntryListPagingSource.newConfig(),
        initialKey = 0,
        pagingSourceFactory = { EntryListPagingSource(getEntries = { page -> searchEntries(query = query, page = page) }).also { pagingSource = it } },
    ).flow.cachedIn(scope)

    override val state = MutableStateFlow<EntrySearchState>(EntrySearchState.Idle)

    init {
        snapshotFlow { this.query }
            .debounce(1.seconds)
            .distinctUntilChanged()
            .onEach { pagingSource.invalidate() }
            .launchIn(scope)

        if (query.isNotBlank()) {
            this.query = query
        }
    }

    override suspend fun handleIntent(intent: EntrySearchIntent) {
        when (intent) {
            is EntrySearchIntent.OpenEntry -> navigateToScreen(EntryFormScreen(entry = intent.entry))
        }
    }
}