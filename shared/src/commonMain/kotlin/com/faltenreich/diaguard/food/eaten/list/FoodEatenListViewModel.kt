package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class FoodEatenListViewModel(
    food: Food,
    getFoodEaten: GetFoodEatenForFoodUseCase = inject(),
) : ViewModel<FoodEatenListViewState, Unit>() {

    override val state = getFoodEaten(food).map { results ->
        when {
            results.isEmpty() -> FoodEatenListViewState.Empty
            else -> FoodEatenListViewState.Loaded(results)
        }
    }

    override fun onIntent(intent: Unit) = Unit
}