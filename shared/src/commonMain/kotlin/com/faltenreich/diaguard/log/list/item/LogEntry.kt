package com.faltenreich.diaguard.log.list.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.data.tag.Tag
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LogEntry(
    state: LogItemState.EntryContent,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onRestore: () -> Unit,
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        LogDay(
            state = state.dayState,
            modifier = Modifier
                .padding(top = AppTheme.dimensions.padding.P_2)
                .width(AppTheme.dimensions.size.LogDayWidth),
        )
        EntryListItem(
            state = state.entryState,
            onClick = onClick,
            onDelete = onDelete,
            onRestore = onRestore,
            onTagClick = onTagClick,
        )
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    LogEntry(
        state = LogItemState.EntryContent(
            dayState = LogDayState(
                date = today(),
                dayOfMonthLocalized = "01",
                dayOfWeekLocalized = "Mon",
                style = LogDayStyle(
                    isVisible = true,
                    isHighlighted = true,
                ),
            ),
            entryState = EntryListItemState(
                entry = entry(),
                dateTimeLocalized = now().toString(),
                foodEatenLocalized = emptyList(),
                categories = emptyList(),
            ),
        ),
        onClick = {},
        onDelete = {},
        onRestore = {},
        onTagClick = {},
    )
}