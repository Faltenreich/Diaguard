package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.food.Food

sealed interface FoodSearchState {

    data object Loading : FoodSearchState

    data class Loaded(val foodList: List<Food>) : FoodSearchState
}