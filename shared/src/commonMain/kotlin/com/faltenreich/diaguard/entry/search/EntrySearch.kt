package com.faltenreich.diaguard.entry.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.LoadingIndicator
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_search_placeholder
import kotlinx.coroutines.launch

@Composable
fun EntrySearch(
    modifier: Modifier = Modifier,
    viewModel: EntrySearchViewModel = inject(),
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (val viewState = viewModel.collectState()) {
            null -> Unit
            is EntrySearchState.Idle ->  Text(getString(Res.string.entry_search_placeholder))
            is EntrySearchState.Loading -> LoadingIndicator()
            is EntrySearchState.Result -> {
                val scope = rememberCoroutineScope()
                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
                    modifier = modifier) {
                    items(items = viewState.items ?: emptyList(), key = Entry.Localized::id) { entry ->
                        Column(modifier = Modifier.animateItem()) {
                            EntryListItem(
                                entry = entry,
                                onClick = { viewModel.dispatchIntent(EntrySearchIntent.OpenEntry(entry)) },
                                onTagClick = { tag ->
                                    viewModel.query = tag.name
                                    scope.launch { listState.scrollToItem(0) }
                                }
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}