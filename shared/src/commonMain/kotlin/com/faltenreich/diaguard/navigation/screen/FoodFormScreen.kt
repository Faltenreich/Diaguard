package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.form.FoodForm
import com.faltenreich.diaguard.shared.di.getViewModel
import org.koin.core.parameter.parametersOf

data class FoodFormScreen(val food: Food? = null) : Screen {

    @Composable
    override fun Content() {
        FoodForm(viewModel = getViewModel { parametersOf(food) })
    }
}