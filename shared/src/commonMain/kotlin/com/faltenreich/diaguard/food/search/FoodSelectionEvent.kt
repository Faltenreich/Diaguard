package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.data.food.Food

sealed interface FoodSelectionEvent {

    data class Select(val food: Food.Local) : FoodSelectionEvent
}