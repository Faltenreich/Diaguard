package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.food.Food

sealed interface FoodSearchIntent {

    data object Refresh : FoodSearchIntent

    data object Close : FoodSearchIntent

    data object Create : FoodSearchIntent

    data class Select(val food: Food) : FoodSearchIntent
}