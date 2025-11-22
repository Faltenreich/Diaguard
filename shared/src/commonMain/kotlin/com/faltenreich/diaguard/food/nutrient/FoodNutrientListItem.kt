package com.faltenreich.diaguard.food.nutrient

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.faltenreich.diaguard.view.FormRow
import com.faltenreich.diaguard.view.TextInput
import com.faltenreich.diaguard.data.preview.AppPreview
import org.jetbrains.compose.resources.stringResource
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
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
            prefix = { Text(stringResource(data.nutrient.label)) },
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