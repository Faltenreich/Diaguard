package com.faltenreich.diaguard.entry.search

import androidx.compose.runtime.snapshotFlow
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.list.EntryListPagingSource
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.seconds

class EntrySearchViewModel(
    query: String = "",
    searchEntries: SearchEntriesUseCase = inject(),
    private val pushScreen: PushScreenUseCase = inject(),
) : ViewModel<EntrySearchState, EntrySearchIntent, Unit>() {

    private val query = MutableStateFlow(query)

    override val state = this.query.map(::EntrySearchState)

    private lateinit var pagingSource: EntryListPagingSource
    val pagingData = this.query.flatMapLatest { query ->
        Pager(
            config = EntryListPagingSource.newConfig(),
            pagingSourceFactory = {
                EntryListPagingSource(getData = { page -> searchEntries(query, page) })
                    .also { pagingSource = it }
            },
        ).flow.cachedIn(scope)
    }

    init {
        snapshotFlow { this.query }
            .debounce(1.seconds)
            .distinctUntilChanged()
            .onEach { pagingSource.invalidate() }
            .launchIn(scope)
    }

    override suspend fun handleIntent(intent: EntrySearchIntent) {
        when (intent) {
            is EntrySearchIntent.SetQuery -> query.update { intent.query }
            is EntrySearchIntent.OpenEntry -> pushScreen(EntryFormScreen(entry = intent.entry))
        }
    }
}