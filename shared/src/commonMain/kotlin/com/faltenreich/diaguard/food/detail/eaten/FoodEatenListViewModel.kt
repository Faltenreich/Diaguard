package com.faltenreich.diaguard.food.detail.eaten

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class FoodEatenListViewModel(
    food: Food,
    getFoodEatenList: GetFoodEatenListUseCase = inject(),
) :ViewModel() {

    val viewState = getFoodEatenList(food).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList(),
    )
}