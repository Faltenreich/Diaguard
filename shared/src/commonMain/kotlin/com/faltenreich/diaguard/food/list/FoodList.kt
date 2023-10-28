package com.faltenreich.diaguard.food.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun FoodList(
    modifier: Modifier = Modifier,
    viewModel: FoodListViewModel = inject(),
) {
    Text("FoodList")
}