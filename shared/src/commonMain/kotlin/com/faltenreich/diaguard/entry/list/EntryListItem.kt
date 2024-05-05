package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.tag.EntryTagList
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
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
            Text(
                text = dateTimeFormatter.formatTime(entry.dateTime.time),
                style = AppTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_2))
            entry.values.forEach { value ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    MeasurementCategoryIcon(value.property.category)
                    Text(
                        text = value.value.toString(),
                    )
                }
            }
            entry.note?.takeIf(String::isNotBlank)?.let { note ->
                Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_2))
                Text(note )
            }
            EntryTagList(
                tags = entry.entryTags.map(EntryTag::tag),
                onTagClick = { tag -> },
            )
        }
    }
}