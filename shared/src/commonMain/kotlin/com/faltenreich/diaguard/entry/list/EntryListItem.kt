package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.view.ClearButton

@Composable
fun EntryListItem(
    modifier: Modifier = Modifier,
    entry: Entry,
    onDelete: (Entry) -> Unit,
) {
    Row(modifier = modifier) {
        Text("Entry ${entry.id}")
        Text(entry.dateTime.toString())
        ClearButton { onDelete(entry) }
    }
}