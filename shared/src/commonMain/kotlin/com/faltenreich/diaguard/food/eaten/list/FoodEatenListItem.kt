package com.faltenreich.diaguard.food.eaten.list

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.view.FormRow
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.grams_abbreviation

@Composable
fun FoodEatenListItem(
    foodEaten: FoodEaten.Local,
    onIntent: (FoodEatenListIntent) -> Unit,
    modifier: Modifier = Modifier,
    // TODO: Remove formatter and pass formatted value instead
    dateTimeFormatter: DateTimeFormatter = inject(),
) {
    FormRow(modifier = modifier.clickable { onIntent(FoodEatenListIntent.OpenEntry(foodEaten.entry)) }) {
        Text(
            text = dateTimeFormatter.formatDateTime(foodEaten.entry.dateTime),
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "%s %s".format(
                // TODO: Localize via NumberFormatter
                foodEaten.amountInGrams.toString(),
                getString(Res.string.grams_abbreviation),
            )
        )
    }
}