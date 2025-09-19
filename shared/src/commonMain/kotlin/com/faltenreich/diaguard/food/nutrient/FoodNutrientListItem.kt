package com.faltenreich.diaguard.food.nutrient

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodNutrientListItem(
    data: FoodNutrientData,
    onUpdate: (FoodNutrientData) -> Unit,
    modifier: Modifier = Modifier,
) {
    var per100g by remember { mutableStateOf(data.per100g) }
    FormRow(modifier = modifier) {
        TextInput(
            input = per100g,
            onInputChange = { input ->
                per100g = input
                onUpdate(data.copy(per100g = input))
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

@Preview
@Composable
private fun Preview() = AppPreview {
    FoodNutrientListItem(
        data = FoodNutrientData(
            nutrient = FoodNutrient.CARBOHYDRATES,
            per100g = "10",
            isLast = false,
        ),
        onUpdate = {},
    )
}