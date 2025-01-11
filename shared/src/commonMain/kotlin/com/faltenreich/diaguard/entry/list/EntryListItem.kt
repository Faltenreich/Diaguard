package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.form.tag.EntryTagList
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.tag.Tag

@Composable
fun EntryListItem(
    state: EntryListItemState,
    onClick: () -> Unit,
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(AppTheme.dimensions.padding.P_3),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
        ) {
            DateTime(state)
            MeasurementValues(state)
            EntryTags(state, onTagClick)
        }

        val note = state.entry.note?.takeIf(String::isNotBlank)
        val foodEaten = state.entry.foodEaten

        if (note != null || foodEaten.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.scheme.surfaceContainerLow)
                    .padding(AppTheme.dimensions.padding.P_3),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
            ) {
                Note(state)
                FoodEaten(state)
            }
        }
    }
}

@Composable
private fun DateTime(state: EntryListItemState) {
    Text(
        text = state.dateTimeLocalized,
        fontWeight = FontWeight.Normal,
        style = AppTheme.typography.titleMedium,
    )
}

@Composable
private fun MeasurementValues(state: EntryListItemState) {
    if (state.values.isNotEmpty()) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2)) {
            state.values.forEach { value ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    MeasurementCategoryIcon(value.category)
                    Text(value.valueLocalized)
                }
            }
        }
    }
}

@Composable
private fun EntryTags(state: EntryListItemState, onTagClick: (Tag) -> Unit) {
    if (state.entry.entryTags.isNotEmpty()) {
        EntryTagList(
            tags = state.entry.entryTags.map(EntryTag::tag),
            onTagClick = onTagClick,
        )
    }
}

@Composable
private fun Note(state: EntryListItemState) {
    state.entry.note?.takeIf(String::isNotBlank)?.let { note ->
        Text(note)
    }
}

@Composable
private fun FoodEaten(state: EntryListItemState) {
    state.foodEatenLocalized.forEach { foodEaten ->
        Text(foodEaten)
    }
}