package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.form.tag.EntryTagList
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.tag.Tag
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.undo
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EntryListItem(
    state: EntryListItemState,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onRestore: () -> Unit,
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    val swipeToDismissState = rememberSwipeToDismissBoxState()
    var alpha by remember { mutableStateOf(1f) }
    var isTouching by remember { mutableStateOf(false) }


    LaunchedEffect(swipeToDismissState.progress) {
        alpha =
            if (swipeToDismissState.currentValue == SwipeToDismissBoxValue.Settled) 1f
            else 1f - swipeToDismissState.progress
    }

    LaunchedEffect(
        swipeToDismissState.progress,
        isTouching,
    ) {
        if (swipeToDismissState.progress == 1f
            && swipeToDismissState.currentValue != SwipeToDismissBoxValue.Settled
            && !isTouching
        ) {
            onDelete()
        }
    }

    SwipeToDismissBox(
        state = swipeToDismissState,
        backgroundContent = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                // TODO: Restore in UI (refresh PagingData?)
                TextButton(onClick = onRestore) {
                    Text(stringResource(Res.string.undo))
                }
            }
        },
        modifier = Modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                awaitFirstDown()
                isTouching = true
                waitForUpOrCancellation()
                isTouching = false
            }
        }
    ) {
        Card(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .alpha(alpha),
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
    if (state.categories.isNotEmpty()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
        ) {
            state.categories.forEach { category ->
                Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3)) {
                    MeasurementCategoryIcon(category.category)

                    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2)) {
                        category.values.forEachIndexed { index, value ->
                            Row(
                                modifier = Modifier.padding(top = AppTheme.dimensions.padding.P_1),
                                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(value.valueLocalized)
                                Text(value.property.unit.abbreviation)

                                if (value.property.name != category.category.name) {
                                    Text(value.property.name)
                                }
                            }
                        }
                    }
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

@Preview
@Composable
private fun Preview() = AppPreview {
    EntryListItem(
        state = EntryListItemState(
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
        onClick = {},
        onDelete = {},
        onRestore = {},
        onTagClick = {},
    )
}