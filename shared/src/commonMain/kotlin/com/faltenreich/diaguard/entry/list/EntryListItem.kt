package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.tag.EntryTagList
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.log.item.LogDay
import com.faltenreich.diaguard.log.item.LogDayStyle
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.Tag

@Composable
fun EntryListItem(
    entry: Entry.Local,
    style: LogDayStyle,
    onClick: () -> Unit,
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
    dateTimeFormatter: DateTimeFormatter = inject(),
) {
    Row(modifier = modifier) {
        LogDay(
            date = entry.dateTime.date,
            style = style,
            modifier = Modifier.width(AppTheme.dimensions.size.LogDayWidth),
        )
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
                            // TODO: Format via MeasurementValueMapper
                            text = value.value.toString(),
                        )
                    }
                }
                EntryTagList(
                    tags = entry.entryTags.map(EntryTag::tag),
                    onTagClick = onTagClick,
                )
                entry.note?.takeIf(String::isNotBlank)?.let { note ->
                    Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_2))
                    Text(note)
                }
                entry.foodEaten.forEach { foodEaten ->
                    Text("${foodEaten.amountInGrams} ${foodEaten.food.name}")
                }
            }
        }
    }
}