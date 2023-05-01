package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.Entry

@Composable
fun EntryList(
    entries: List<Entry>,
    modifier: Modifier = Modifier,
    onDelete: (Entry) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(items = entries, key = Entry::id) { entry ->
            Column(
                modifier = Modifier.animateItemPlacement(),
            ) {
                EntryListItem(
                    entry,
                    onDelete = onDelete,
                )
                Divider()
            }
        }
    }
}