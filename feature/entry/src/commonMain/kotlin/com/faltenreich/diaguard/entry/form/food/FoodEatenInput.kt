package com.faltenreich.diaguard.entry.form.food

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

@Composable
fun FoodEatenInput(
    state: FoodEatenInputState,
    keyboardOptions: KeyboardOptions,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var input by remember { mutableStateOf(state.amountInGrams) }

    TextInput(
        input = input,
        onInputChange = {
            input = it
            onIntent(EntryFormIntent.EditFood(state.copy(amountInGrams = it)))
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
        keyboardOptions = keyboardOptions,
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
        keyboardOptions = KeyboardOptions.Default,
        onIntent = {},
    )
}