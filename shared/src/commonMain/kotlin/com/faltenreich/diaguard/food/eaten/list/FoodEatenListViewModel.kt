package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.navigation.NavigateToUseCase
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class FoodEatenListViewModel(
    food: Food,
    getFoodEaten: GetFoodEatenForFoodUseCase = inject(),
    private val navigateTo: NavigateToUseCase = inject(),
) : ViewModel<FoodEatenListViewState, FoodEatenListIntent>() {

    override val state = getFoodEaten(food).map { results ->
        when {
            results.isEmpty() -> FoodEatenListViewState.Empty
            else -> FoodEatenListViewState.Loaded(results)
        }
    }

    override fun onIntent(intent: FoodEatenListIntent) {
        when (intent) {
            is FoodEatenListIntent.CreateEntry -> navigateTo(EntryFormScreen(food = intent.food))
            is FoodEatenListIntent.OpenEntry -> navigateTo(EntryFormScreen(entry = intent.entry))
        }
    }
}