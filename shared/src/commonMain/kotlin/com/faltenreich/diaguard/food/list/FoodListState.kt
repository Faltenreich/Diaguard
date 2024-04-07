package com.faltenreich.diaguard.food.list

import com.faltenreich.diaguard.food.Food

sealed interface FoodListState {

    data object Loading : FoodListState

    data class Loaded(val foodList: List<Food>) : FoodListState
}