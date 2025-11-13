package com.faltenreich.diaguard.entry.search

import androidx.paging.Pager
import androidx.paging.cachedIn
import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.form.StoreEntryUseCase
import com.faltenreich.diaguard.entry.list.EntryListPagingSource
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.core.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class EntrySearchViewModel(
    initialQuery: String,
    searchEntries: SearchEntriesUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
    private val storeEntry: StoreEntryUseCase = inject(),
    private val pushScreen: PushScreenUseCase = inject(),
) : ViewModel<EntrySearchState, EntrySearchIntent, Unit>() {

    private val query = MutableStateFlow(initialQuery)
    private lateinit var pagingSource: EntryListPagingSource

    override val state = query.map { query ->
        EntrySearchState(
            query = query,
            pagingData = Pager(
                config = EntryListPagingSource.newConfig(),
                pagingSourceFactory = {
                    EntryListPagingSource(getData = { page -> searchEntries(query, page) })
                        .also { pagingSource = it }
                },
            ).flow.cachedIn(scope),
        )
    }

    override suspend fun handleIntent(intent: EntrySearchIntent) {
        when (intent) {
            is EntrySearchIntent.SetQuery -> query.update { intent.query }
            is EntrySearchIntent.OpenEntry -> pushScreen(EntryFormScreen(entry = intent.entry))
            is EntrySearchIntent.DeleteEntry -> deleteEntry(intent.entry)
            is EntrySearchIntent.RestoreEntry -> {
                storeEntry(intent.entry)
                pagingSource.invalidate()
            }
        }
    }
}