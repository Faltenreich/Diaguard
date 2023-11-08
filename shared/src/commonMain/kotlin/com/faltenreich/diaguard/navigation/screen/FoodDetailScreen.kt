package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.detail.FoodDetail
import com.faltenreich.diaguard.shared.di.getViewModel
import org.koin.core.parameter.parametersOf

data class FoodDetailScreen(val food: Food) : Screen() {

    @Composable
    override fun Content() {
        FoodDetail(viewModel = getViewModel { parametersOf(food) })
    }
}