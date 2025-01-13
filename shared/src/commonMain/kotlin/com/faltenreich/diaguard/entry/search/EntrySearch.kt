package com.faltenreich.diaguard.entry.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.entry.list.EntryList
import kotlinx.coroutines.launch

@Composable
fun EntrySearch(
    viewModel: EntrySearchViewModel,
    modifier: Modifier = Modifier,
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
        modifier = modifier.fillMaxSize(),
    )
}