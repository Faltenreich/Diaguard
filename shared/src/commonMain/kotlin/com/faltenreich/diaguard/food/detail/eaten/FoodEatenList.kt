package com.faltenreich.diaguard.food.detail.eaten

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

@Composable
fun FoodEatenList(
    modifier: Modifier = Modifier,
    viewModel: FoodEatenListViewModel,
) {
    val viewState = viewModel.viewState.collectAsState().value
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        viewState.forEach { foodEaten ->
            FoodEatenListItem(foodEaten)
            Divider()
        }
    }
}