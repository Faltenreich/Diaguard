package com.faltenreich.diaguard.log.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.log.LogIntent

@Composable
fun LogEntry(
    entry: Entry,
    onIntent: (LogIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    EntryListItem(
        entry = entry,
        modifier = modifier
            .clickable(onClick = { onIntent(LogIntent.OpenEntry(entry)) })
            .padding(all = AppTheme.dimensions.padding.P_2),
    )
}