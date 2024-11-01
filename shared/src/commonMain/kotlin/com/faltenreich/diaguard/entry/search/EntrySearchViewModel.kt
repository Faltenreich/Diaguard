package com.faltenreich.diaguard.entry.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.list.EntryListPagingSource
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.seconds

class EntrySearchViewModel(
    query: String = "",
    searchEntries: SearchEntriesUseCase = inject(),
    private val pushScreen: PushScreenUseCase = inject(),
) : ViewModel<Unit, EntrySearchIntent, Unit>() {

    override val state = emptyFlow<Unit>()

    var query: String by mutableStateOf("")

    private lateinit var pagingSource: EntryListPagingSource
    val pagingData = Pager(
        config = EntryListPagingSource.newConfig(),
        pagingSourceFactory = {
            EntryListPagingSource(getData = { page -> searchEntries(this.query, page) })
                .also { pagingSource = it }
        },
    ).flow.cachedIn(scope)

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
            is EntrySearchIntent.OpenEntry -> pushScreen(EntryFormScreen(entry = intent.entry))
        }
    }
}