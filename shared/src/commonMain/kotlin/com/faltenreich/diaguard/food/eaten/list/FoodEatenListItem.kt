package com.faltenreich.diaguard.food.eaten.list

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.view.FormRow

@Composable
fun FoodEatenListItem(
    foodEaten: FoodEaten,
    modifier: Modifier = Modifier,
    dateTimeFormatter: DateTimeFormatter = inject(),
    navigation: Navigation = inject(),
) {
    FormRow(modifier = modifier.clickable { navigation.push(EntryFormScreen(foodEaten.entry)) }) {
        Text(
            text = dateTimeFormatter.formatDateTime(foodEaten.entry.dateTime),
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "%d %s".format(
                foodEaten.amountInGrams,
                getString(MR.strings.grams_abbreviation),
            )
        )
    }
}