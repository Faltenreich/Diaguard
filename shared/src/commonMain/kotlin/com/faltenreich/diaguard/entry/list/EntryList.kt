package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.log.item.LogLoadingIndicator
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.tag.Tag
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_search_placeholder

@Composable
fun EntryList(
    items: LazyPagingItems<EntryListItemState>,
    onEntryClick: (Entry.Local) -> Unit,
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (items.itemCount == 0) {
            if (items.loadState.refresh == LoadState.Loading) {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Text(getString(Res.string.entry_search_placeholder))
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(
                    count = items.itemCount,
                    key = { index -> checkNotNull(items.peek(index)).entry.id },
                ) { index ->
                    val item = checkNotNull(items[index])

                    EntryListItem(
                        state = item,
                        onClick = { onEntryClick(item.entry) },
                        onTagClick = onTagClick,
                        modifier = Modifier
                            .animateItem()
                            .fillMaxWidth()
                            .padding(
                                horizontal = AppTheme.dimensions.padding.P_3,
                                vertical = AppTheme.dimensions.padding.P_2,
                            ),
                    )
                }

                if (items.loadState.append == LoadState.Loading) {
                    item {
                        LogLoadingIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}