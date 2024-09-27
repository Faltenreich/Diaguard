package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.tag.Tag

@Composable
fun EntryList(
    items: List<Entry.Local>,
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

        items(items = items, key = Entry.Local::id) { entry ->
            EntryListItem(
                entry = entry,
                onClick = { onEntryClick(entry) },
                onTagClick = onTagClick,
                modifier = Modifier
                    .padding(
                        start = AppTheme.dimensions.padding.P_2,
                        top = AppTheme.dimensions.padding.P_2,
                        end = AppTheme.dimensions.padding.P_2,
                    )
                    .animateItem(),
            )
        }
    }
}