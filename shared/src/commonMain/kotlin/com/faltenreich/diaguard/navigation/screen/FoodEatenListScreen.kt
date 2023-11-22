package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.list.FoodEatenList
import com.faltenreich.diaguard.shared.di.getViewModel
import org.koin.core.parameter.parametersOf

data class FoodEatenListScreen(val food: Food) : Screen() {

    @Composable
    override fun Content() {
        FoodEatenList(viewModel = getViewModel { parametersOf(food) })
    }
}