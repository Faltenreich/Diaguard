package com.faltenreich.diaguard.log.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.tag.Tag

@Composable
fun LogEntry(
    state: LogItemState.EntryContent,
    onClick: () -> Unit,
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    EntryListItem(
        entry = state.entry,
        style = state.style,
        onClick = onClick,
        onTagClick = onTagClick,
        modifier = modifier,
    )
}