package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class FoodEatenListViewModel(
    food: Food.Local,
    getFoodEaten: GetFoodEatenForFoodUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
) : ViewModel<FoodEatenListViewState, FoodEatenListIntent, Unit>() {

    override val state = getFoodEaten(food).map { results ->
        when {
            results.isEmpty() -> FoodEatenListViewState.Empty
            else -> FoodEatenListViewState.Loaded(results)
        }
    }

    override suspend fun handleIntent(intent: FoodEatenListIntent) {
        when (intent) {
            is FoodEatenListIntent.CreateEntry -> navigateToScreen(EntryFormScreen(food = intent.food))
            is FoodEatenListIntent.OpenEntry -> navigateToScreen(EntryFormScreen(entry = intent.entry))
        }
    }
}