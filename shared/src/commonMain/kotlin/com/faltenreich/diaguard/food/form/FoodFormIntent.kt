package com.faltenreich.diaguard.food.form

sealed interface FoodFormIntent {

    data object Submit : FoodFormIntent

    data object Delete : FoodFormIntent
}