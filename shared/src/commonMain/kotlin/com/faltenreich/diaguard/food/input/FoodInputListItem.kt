package com.faltenreich.diaguard.food.input

import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon

@Composable
fun FoodInputListItem(
    data: FoodInputData,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    FormRow(modifier = modifier) {
        Text(
            text = data.food.name,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = { onIntent(EntryFormIntent.RemoveFood(data)) }) {
            ResourceIcon(MR.images.ic_remove)
        }
    }
}