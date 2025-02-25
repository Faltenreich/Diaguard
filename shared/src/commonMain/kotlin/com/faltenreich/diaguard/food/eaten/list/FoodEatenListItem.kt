package com.faltenreich.diaguard.food.eaten.list

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.shared.view.FormRow

@Composable
fun FoodEatenListItem(
    foodEaten: FoodEaten.Localized,
    onIntent: (FoodEatenListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    FormRow(
        modifier = modifier.clickable {
            onIntent(FoodEatenListIntent.OpenEntry(foodEaten.local.entry))
        },
    ) {
        Text(
            text = foodEaten.dateTime,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = foodEaten.amountInGrams,
        )
    }
}