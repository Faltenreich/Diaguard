package com.faltenreich.diaguard.entry.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
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
                    modifier = modifier
                        .fillMaxSize()
                        .padding(all = AppTheme.dimensions.padding.P_2),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
                ) {
                    items(items = viewState.items ?: emptyList(), key = Entry.Local::id) { entry ->
                        EntryListItem(
                            entry = entry,
                            onClick = { viewModel.dispatchIntent(EntrySearchIntent.OpenEntry(entry)) },
                            onTagClick = { tag ->
                                viewModel.query = tag.name
                                scope.launch { listState.scrollToItem(0) }
                            },
                            modifier = Modifier.animateItem(),
                        )
                    }
                }
            }
        }
    }
}