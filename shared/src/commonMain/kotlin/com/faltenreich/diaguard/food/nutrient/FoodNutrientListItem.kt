package com.faltenreich.diaguard.food.nutrient

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.food.form.FoodFormIntent
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun FoodNutrientListItem(
    data: FoodNutrientData,
    onIntent: (FoodFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    FormRow(modifier = modifier) {
        TextInput(
            input = data.per100g,
            onInputChange = { input ->
                onIntent(FoodFormIntent.EditNutrient(data.copy(per100g = input)))
            },
            label = getString(data.nutrient.label),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = if (data.isLast) ImeAction.Done else ImeAction.Next,
            ),
            modifier = Modifier.weight(1f),
        )
    }
}