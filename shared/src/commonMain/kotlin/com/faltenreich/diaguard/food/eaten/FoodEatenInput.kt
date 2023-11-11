package com.faltenreich.diaguard.food.eaten

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.view.ClearButton
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun FoodEatenInput(
    data: FoodEatenInputData,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    FormRow(modifier = modifier) {
        Spacer(modifier = Modifier.size(AppTheme.dimensions.size.ImageMedium))
        TextInput(
            // FIXME: Input is broken
            input = data.amountInGrams?.toString() ?: "",
            onInputChange = { input ->
                onIntent(EntryFormIntent.EditFood(data.copy(amountInGrams = input.toIntOrNull())))
            },
            label = data.food.name,
            modifier = Modifier.weight(1f),
            trailingIcon = { ClearButton { onIntent(EntryFormIntent.RemoveFood(data)) } },
            supportingText = {
                Text("%.2f %s".format(
                    // TODO: Convert into selected unit
                    // valueFormatter.formatValue(data.food.carbohydrates, factor)
                    data.food.carbohydrates,
                    getString(MR.strings.carbohydrates_per_100g)),
                )
         },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                // FIXME: Might be ImeAction.Done if meal is last property
                imeAction = ImeAction.Next,
            ),
        )
    }
}