package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FoodEatenListViewModel(
    food: Food,
    getFoodEaten: GetFoodEatenForFoodUseCase = inject(),
) :ViewModel() {

    private val state = getFoodEaten(food).map { results ->
        when {
            results.isEmpty() -> FoodEatenListViewState.Empty
            else -> FoodEatenListViewState.Loaded(results)
        }
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = FoodEatenListViewState.Loading,
    )
}