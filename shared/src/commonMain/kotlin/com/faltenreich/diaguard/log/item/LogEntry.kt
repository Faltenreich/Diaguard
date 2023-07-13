package com.faltenreich.diaguard.log.item

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.list.EntryListItem

@Composable
fun LogEntry(
    entry: Entry,
    modifier: Modifier = Modifier,
) {
    EntryListItem(
        entry = entry,
        modifier = modifier.padding(all = AppTheme.dimensions.padding.P_2),
    )
}