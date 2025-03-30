package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.food.Food

sealed interface FoodSearchIntent {

    data object Close : FoodSearchIntent

    data object Create : FoodSearchIntent

    data class OpenFood(val food: Food.Local) : FoodSearchIntent

    data object OpenPreferences : FoodSearchIntent
}