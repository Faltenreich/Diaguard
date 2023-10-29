package com.faltenreich.diaguard.food.detail.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.measurement.value.MeasurementValueFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon

@Composable
fun FoodInfo(
    food: Food,
    modifier: Modifier = Modifier,
    valueFormatter: MeasurementValueFormatter = inject(),
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        FormRow(icon = { ResourceIcon(MR.images.ic_brand) }) {
            Text(food.brand ?: getString(MR.strings.placeholder))
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_ingredients) }) {
            Text(food.ingredients ?: getString(MR.strings.placeholder))
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_meal) }) {
            Text(getString(MR.strings.food_carbohydrates_info,
                // TODO: Format per user setting
                valueFormatter.formatValue(food.carbohydrates, factor = 1.0),
                getString(MR.strings.carbohydrates_abbreviation),
            ))
        }
        // TODO: Format and add additionals labels
        food.labels?.let { labels ->
            Divider()
            FormRow(icon = { ResourceIcon(MR.images.ic_info) }) {
                Text(labels)
            }
        }
    }
}