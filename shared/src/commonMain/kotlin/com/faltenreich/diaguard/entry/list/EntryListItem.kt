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
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.tag.EntryTagList
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.tag.Tag

@Composable
fun EntryListItem(
    entry: Entry.Local,
    onClick: () -> Unit,
    onTagClick: (Tag) -> Unit,
    dateTime: @Composable () -> Unit,
    modifier: Modifier = Modifier,
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
            dateTime()

            Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_2))

            MeasurementValues(entry)

            EntryTags(entry, onTagClick)

            Note(entry)

            FoodEaten(entry)
        }
    }
}

@Composable
private fun MeasurementValues(entry: Entry.Local) {
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
}

@Composable
private fun EntryTags(entry: Entry.Local, onTagClick: (Tag) -> Unit) {
    EntryTagList(
        tags = entry.entryTags.map(EntryTag::tag),
        onTagClick = onTagClick,
    )
}

@Composable
private fun Note(entry: Entry.Local) {
    entry.note?.takeIf(String::isNotBlank)?.let { note ->
        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_2))
        Text(note)
    }
}

@Composable
private fun FoodEaten(entry: Entry.Local) {
    entry.foodEaten.forEach { foodEaten ->
        Text("${foodEaten.amountInGrams} ${foodEaten.food.name}")
    }
}