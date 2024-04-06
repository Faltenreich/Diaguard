package com.faltenreich.diaguard.food.list

import com.faltenreich.diaguard.food.Food

sealed interface FoodListIntent {

    data object Create : FoodListIntent

    data class Select(val food: Food) : FoodListIntent
}