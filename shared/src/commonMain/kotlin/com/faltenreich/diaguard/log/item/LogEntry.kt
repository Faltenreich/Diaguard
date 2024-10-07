package com.faltenreich.diaguard.log.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.Tag

@Composable
fun LogEntry(
    state: LogItemState.EntryContent,
    onClick: () -> Unit,
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
    // TODO: Format in ViewModel
    dateTimeFormatter: DateTimeFormatter = inject(),
) {
    Row(modifier = modifier) {
        LogDay(
            date = state.entry.dateTime.date,
            style = state.style,
            modifier = Modifier.width(AppTheme.dimensions.size.LogDayWidth),
        )
        EntryListItem(
            entry = state.entry,
            onClick = onClick,
            onTagClick = onTagClick,
            dateTime = {
                Text(
                    text = dateTimeFormatter.formatTime(state.entry.dateTime.time),
                    style = AppTheme.typography.titleMedium,
                )
            }
        )
    }
}