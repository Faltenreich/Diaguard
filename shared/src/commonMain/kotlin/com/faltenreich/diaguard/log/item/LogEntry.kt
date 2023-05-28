package com.faltenreich.diaguard.log.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.list.EntryListItem

@Composable
fun LogEntry(
    entry: Entry,
    onDelete: (Entry) -> Unit,
    modifier: Modifier = Modifier,
) {
    EntryListItem(
        entry = entry,
        onDelete = onDelete,
        modifier = modifier,
    )
}