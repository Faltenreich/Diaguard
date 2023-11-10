package com.faltenreich.diaguard.food.input

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FoodInputListItem(
    data: FoodInputData,
    modifier: Modifier = Modifier,
) {
    Text(data.food.name, modifier)
}