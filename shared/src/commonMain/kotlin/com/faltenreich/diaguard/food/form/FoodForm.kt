package com.faltenreich.diaguard.food.form

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun FoodForm(
    modifier: Modifier = Modifier,
    viewModel: FoodFormViewModel = inject(),
) {
    Text("Food Form for ${viewModel.food?.name ?: "New food"}", modifier = modifier)
}