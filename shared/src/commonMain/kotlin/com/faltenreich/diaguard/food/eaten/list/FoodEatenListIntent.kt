package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.food.Food

sealed interface FoodEatenListIntent {

    data class CreateEntry(val food: Food) : FoodEatenListIntent

    data class OpenEntry(val entry: Entry) : FoodEatenListIntent
}