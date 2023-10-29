package com.faltenreich.diaguard.food.detail.info

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.food.Food

@Composable
fun FoodInfo(
    food: Food,
    modifier: Modifier = Modifier,
) {
    Text("Food Info", modifier)
}