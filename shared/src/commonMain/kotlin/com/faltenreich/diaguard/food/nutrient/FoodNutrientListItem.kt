package com.faltenreich.diaguard.food.nutrient

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            input = data.per100g?.toString() ?: "",
            onInputChange = { input ->
                onIntent(FoodFormIntent.EditNutrient(data.copy(per100g = input.toDoubleOrNull())))
            },
            label = getString(data.nutrient.label),
        )
    }
}