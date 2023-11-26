package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.food.eaten.FoodEaten

sealed interface FoodEatenListViewState {

    data object Loading : FoodEatenListViewState

    data class Loaded(val results: List<FoodEaten>) : FoodEatenListViewState

    data object Empty : FoodEatenListViewState
}