package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.food.eaten.FoodEaten

sealed interface FoodEatenListState {

    data object Empty : FoodEatenListState

    data class NonEmpty(val results: List<FoodEaten>) : FoodEatenListState
}