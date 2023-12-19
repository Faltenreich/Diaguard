package com.faltenreich.diaguard.food.list

import com.faltenreich.diaguard.food.Food

sealed interface FoodListIntent {

    data object Close : FoodListIntent

    data class EditFood(val food: Food) : FoodListIntent
}