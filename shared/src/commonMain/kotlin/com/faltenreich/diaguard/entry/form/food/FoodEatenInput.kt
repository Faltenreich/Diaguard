package com.faltenreich.diaguard.entry.form.food

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.food_delete
import com.faltenreich.diaguard.resource.grams_abbreviation
import com.faltenreich.diaguard.resource.ic_delete
import com.faltenreich.diaguard.view.input.TextInput
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodEatenInput(
    state: FoodEatenInputState,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextInput(
        input = state.amountInGrams,
        onInputChange = { input ->
            onIntent(EntryFormIntent.EditFood(state.copy(amountInGrams = input)))
        },
        label = state.food.name,
        modifier = modifier.padding(vertical = AppTheme.dimensions.padding.P_1),
        placeholder = { Text(stringResource(Res.string.grams_abbreviation)) },
        trailingIcon = {
            IconButton(onClick = { onIntent(EntryFormIntent.RemoveFood(state)) }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = stringResource(Res.string.food_delete),
                )
            }
        },
        supportingText = { Text(state.amountPer100g) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            // FIXME: Might be ImeAction.Done if meal is last category
            imeAction = ImeAction.Next,
        ),
    )
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    FoodEatenInput(
        state = FoodEatenInputState(
            food = food(),
            amountPer100g = "8",
            amountInGrams = "100",
        ),
        onIntent = {},
    )
}