package com.faltenreich.diaguard.timeline.entry

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimelineEntryBottomSheet(
    entries: List<EntryListItemState>,
    onEntryClick: (Entry.Local) -> Unit,
    onEntryDelete: (Entry.Local) -> Unit,
    onEntryRestore: (EntryListItemState) -> Unit,
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.padding(bottom = AppTheme.dimensions.padding.P_2)) {
        items(entries) { entry ->
            EntryListItem(
                state = entry,
                onClick = { onEntryClick(entry.entry) },
                onDelete = { onEntryDelete(entry.entry) },
                onRestore = { onEntryRestore(entry) },
                onTagClick = onTagClick,
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppTheme.dimensions.padding.P_3,
                        vertical = AppTheme.dimensions.padding.P_2,
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    TimelineEntryBottomSheet(
        entries = listOf(
            EntryListItemState(
                entry = entry(),
                dateTimeLocalized = now().toString(),
                foodEatenLocalized = emptyList(),
                categories = listOf(
                    EntryListItemState.Category(
                        category = category(),
                        values = listOf(
                            EntryListItemState.Value(
                                property = property(),
                                value = value(),
                                valueLocalized = value().value.toString(),
                            ),
                        ),
                    ),
                ),
            ),
        ),
        onEntryClick = {},
        onEntryDelete = {},
        onEntryRestore = {},
        onTagClick = {},
    )
}