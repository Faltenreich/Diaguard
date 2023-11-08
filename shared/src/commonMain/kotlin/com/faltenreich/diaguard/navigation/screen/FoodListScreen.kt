package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.food.list.FoodList
import com.faltenreich.diaguard.shared.di.getViewModel

data object FoodListScreen : Screen() {

    @Composable
    override fun Content() {
        FoodList(viewModel = getViewModel())
    }
}