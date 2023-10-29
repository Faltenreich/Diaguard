package com.faltenreich.diaguard.food.detail.nutrient

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun FoodNutrientList(
    modifier: Modifier = Modifier,
    viewModel: FoodNutrientListViewModel = inject(),
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Text(getString(MR.strings.food_nutrients_per_100g))
        viewModel.data.forEach { data ->
            FoodNutrientListItem(data)
        }
    }
}