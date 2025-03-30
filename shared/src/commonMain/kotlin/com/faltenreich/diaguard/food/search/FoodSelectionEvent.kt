package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.food.Food

sealed interface FoodSelectionEvent {

    data class Select(val food: Food.Local) : FoodSelectionEvent
}