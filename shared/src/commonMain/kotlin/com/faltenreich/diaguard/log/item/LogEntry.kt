package com.faltenreich.diaguard.log.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.tag.Tag

@Composable
fun LogEntry(
    state: LogItemState.EntryContent,
    onClick: () -> Unit,
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        LogDay(
            date = state.date,
            style = state.style,
            modifier = Modifier.width(AppTheme.dimensions.size.LogDayWidth),
        )
        EntryListItem(
            state = state.entryState,
            onClick = onClick,
            onTagClick = onTagClick,
        )
    }
}