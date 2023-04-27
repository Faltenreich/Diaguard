package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.datetime.DateTimeApi
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.ClearButton

@Composable
fun EntryListItem(
    entry: Entry,
    modifier: Modifier = Modifier,
    onDelete: (Entry) -> Unit,
) {
    val dateTimeApi = inject<DateTimeApi>()
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = entry.id.toString(),
            modifier = Modifier.width(48.dp),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(dateTimeApi.dateTimeToLocalizedString(entry.dateTime))
            Text(entry.note ?: "")
        }
        ClearButton { onDelete(entry) }
    }
}