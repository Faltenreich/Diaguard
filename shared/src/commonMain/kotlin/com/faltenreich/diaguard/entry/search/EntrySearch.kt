package com.faltenreich.diaguard.entry.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.entry.list.EntryList
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
        val items = viewModel.pagingData.collectAsLazyPagingItems()
        val scope = rememberCoroutineScope()
        val listState = rememberLazyListState()

        EntryList(
            items = items,
            onEntryClick = { entry ->
                viewModel.dispatchIntent(EntrySearchIntent.OpenEntry(entry))
            },
            onTagClick = { tag ->
                viewModel.query = tag.name
                scope.launch { listState.scrollToItem(0) }
            },
            listState = listState,
        )

        return

        when {
            items.loadState.isIdle -> Text(getString(Res.string.entry_search_placeholder))
            items.loadState.refresh is LoadState.Loading -> LoadingIndicator()
            else -> {
            }
        }
    }
}