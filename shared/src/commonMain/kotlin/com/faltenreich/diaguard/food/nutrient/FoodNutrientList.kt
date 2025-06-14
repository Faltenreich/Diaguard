package com.faltenreich.diaguard.food.nutrient

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.food.form.FoodFormIntent
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.TextDivider
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.nutrients_per_100g

@Composable
fun FoodNutrientList(
    data: List<FoodNutrientData>,
    onIntent: (FoodFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        TextDivider(getString(Res.string.nutrients_per_100g))

        data.forEach { data ->
            FoodNutrientListItem(
                data = data,
                onIntent = onIntent,
            )
            Divider()
        }
    }
}