package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.list.DateListItemStyle
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.tag.Tag

@Composable
fun EntryList(
    items: LazyPagingItems<Entry.Local>,
    onEntryClick: (Entry.Local) -> Unit,
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    header: LazyListScope.() -> Unit = {},
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
    ) {
        header()

        for (index in 0 until items.itemCount) {
            val peek = checkNotNull(items.peek(index))
            item(key = peek.id) {
                val entry = checkNotNull(items[index])
                EntryListItem(
                    entry = entry,
                    // TODO: Determine in ViewModel
                    style = DateListItemStyle(
                        isVisible = true,
                        isHighlighted = false,
                    ),
                    onClick = { onEntryClick(entry) },
                    onTagClick = onTagClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = AppTheme.dimensions.padding.P_3,
                            vertical = AppTheme.dimensions.padding.P_2,
                        )
                        .animateItem(),
                )
            }
        }
    }
}