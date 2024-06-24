package com.faltenreich.diaguard.log.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.list.EntryListItem

@Composable
fun LogEntry(
    item: LogItem.EntryContent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        LogDay(
            date = item.date,
            style = item.style,
            modifier = Modifier.width(AppTheme.dimensions.size.LogDayWidth),
        )
        EntryListItem(
            entry = item.entry,
            onClick = onClick,
        )
    }
}