package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.food.Food

sealed interface FoodSearchState {

    data object Loading : FoodSearchState

    data object Empty : FoodSearchState

    data class Loaded(
        val items: List<Food>,
    ) : FoodSearchState
}