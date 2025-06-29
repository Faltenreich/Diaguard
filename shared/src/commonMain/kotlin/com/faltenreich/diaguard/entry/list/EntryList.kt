package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.tag.Tag
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EntryList(
    items: LazyPagingItems<EntryListItemState>,
    emptyContent: @Composable () -> Unit,
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
                emptyContent()
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
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    EntryList(
        items = listOf(
            EntryListItemState(
                entry = entry(),
                dateTimeLocalized = now().toString(),
                foodEatenLocalized = emptyList(),
                categories = listOf(
                    EntryListItemState.Category(
                        category = category(),
                        values = listOf(
                            EntryListItemState.Value(
                                property = property(),
                                valueLocalized = value().value.toString(),
                            ),
                        ),
                    ),
                ),
            )
        ).toLazyPagingItems(),
        emptyContent = {},
        onEntryClick = {},
        onTagClick = {},
    )
}