package com.faltenreich.diaguard.food.eaten

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.carbohydrates_per_100g
import diaguard.shared.generated.resources.food_delete
import diaguard.shared.generated.resources.grams_abbreviation
import diaguard.shared.generated.resources.ic_delete
import org.jetbrains.compose.resources.painterResource

@Composable
fun FoodEatenInput(
    data: FoodEatenInputState,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextInput(
        input = data.amountInGrams,
        onInputChange = { input ->
            onIntent(EntryFormIntent.EditFood(data.copy(amountInGrams = input)))
        },
        label = data.food.name,
        modifier = modifier.padding(vertical = AppTheme.dimensions.padding.P_1),
        placeholder = { Text(getString(Res.string.grams_abbreviation)) },
        trailingIcon = {
            IconButton(onClick = { onIntent(EntryFormIntent.RemoveFood(data)) }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = getString(Res.string.food_delete),
                )
            }
        },
        supportingText = {
            Text(
                "%s %s".format(
                    data.amountPer100g,
                    // TODO: Format with selected unit
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