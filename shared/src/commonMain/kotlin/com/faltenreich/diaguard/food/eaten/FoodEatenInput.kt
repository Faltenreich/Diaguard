package com.faltenreich.diaguard.food.eaten

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.view.ClearButton
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.carbohydrates_per_100g

@Composable
fun FoodEatenInput(
    data: FoodEatenInputState,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextInput(
        input = data.amountInGrams?.toString() ?: "",
        onInputChange = { input ->
            onIntent(EntryFormIntent.EditFood(data.copy(amountInGrams = input.toDoubleOrNull())))
        },
        label = data.food.name,
        modifier = modifier,
        trailingIcon = { ClearButton { onIntent(EntryFormIntent.RemoveFood(data)) } },
        supportingText = {
            Text(
                "%.2f %s".format(
                    // TODO: Convert into selected unit
                    // valueFormatter.formatValue(data.food.carbohydrates, factor)
                    data.food.carbohydrates,
                    getString(Res.string.carbohydrates_per_100g)
                ),
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            // FIXME: Might be ImeAction.Done if meal is last category
            imeAction = ImeAction.Next,
        ),
    )
}