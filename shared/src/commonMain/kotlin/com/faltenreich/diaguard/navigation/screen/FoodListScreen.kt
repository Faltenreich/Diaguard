package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.list.FoodList
import com.faltenreich.diaguard.shared.di.getViewModel

data class FoodListScreen(private val onSelection: ((Food) -> Unit)? = null) : Screen {

    @Composable
    override fun Content() {
        FoodList(
            onSelection = onSelection,
            viewModel = getViewModel(),
        )
    }
}