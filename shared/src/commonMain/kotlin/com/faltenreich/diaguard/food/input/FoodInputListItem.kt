package com.faltenreich.diaguard.food.input

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.shared.view.ClearButton
import com.faltenreich.diaguard.shared.view.FormRow

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
        ClearButton { onIntent(EntryFormIntent.RemoveFood(data)) }
    }
}