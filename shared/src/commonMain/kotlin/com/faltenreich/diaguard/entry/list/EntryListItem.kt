package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.tag.EntryTagList
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.EntryTag

@Composable
fun EntryListItem(
    entry: Entry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    dateTimeFormatter: DateTimeFormatter = inject(),
) {
    Card(
        onClick = onClick,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimensions.padding.P_3),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
        ) {
            Text(dateTimeFormatter.formatTime(entry.dateTime.time))
            entry.values.forEach { value ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    MeasurementPropertyIcon(value.type.property)
                    Text(value.value.toString())
                }
            }
            EntryTagList(
                tags = entry.entryTags.map(EntryTag::tag),
                onTagClick = { tag -> },
            )
        }
    }
}