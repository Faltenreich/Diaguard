package com.faltenreich.diaguard.food.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun FoodDetail(
    modifier: Modifier = Modifier,
    viewModel: FoodDetailViewModel = inject(),
) {
    Text("Food Detail")
}