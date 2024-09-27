package com.faltenreich.diaguard.log.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.tag.Tag

@Composable
fun LogEntry(
    item: LogItem.EntryContent,
    onClick: () -> Unit,
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    EntryListItem(
        entry = item.entry,
        style = item.style,
        onClick = onClick,
        onTagClick = onTagClick,
        modifier = modifier,
    )
}