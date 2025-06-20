package com.faltenreich.diaguard.timeline.entry

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.tag.Tag

@Composable
fun TimelineEntryBottomSheet(
    entries: List<EntryListItemState>,
    onEntryClick: (Entry.Local) -> Unit,
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.padding(bottom = AppTheme.dimensions.padding.P_2)) {
        items(entries) { entry ->
            EntryListItem(
                state = entry,
                onClick = { onEntryClick(entry.entry) },
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
    }
}