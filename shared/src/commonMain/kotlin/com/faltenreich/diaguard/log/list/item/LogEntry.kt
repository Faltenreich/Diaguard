package com.faltenreich.diaguard.log.list.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.tag.Tag
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            modifier = Modifier
                .padding(top = AppTheme.dimensions.padding.P_2)
                .width(AppTheme.dimensions.size.LogDayWidth),
        )
        EntryListItem(
            state = state.entryState,
            onClick = onClick,
            onTagClick = onTagClick,
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    LogEntry(
        state = LogItemState.EntryContent(
            entryState = EntryListItemState(
                entry = entry(),
                dateTimeLocalized = now().toString(),
                foodEatenLocalized = emptyList(),
                categories = emptyList(),
            ),
            style = LogDayStyle(
                isVisible = true,
                isHighlighted = true,
            ),
        ),
        onClick = {},
        onTagClick = {},
    )
}