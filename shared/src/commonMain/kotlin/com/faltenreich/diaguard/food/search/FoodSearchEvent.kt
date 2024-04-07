package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.food.Food

sealed interface FoodSearchEvent {

    data class Select(val food: Food) : FoodSearchEvent
}