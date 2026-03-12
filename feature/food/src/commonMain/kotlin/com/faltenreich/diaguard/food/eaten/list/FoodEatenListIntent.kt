package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.data.entry.Entry

sealed interface FoodEatenListIntent {

    data object CreateEntry : FoodEatenListIntent

    data class OpenEntry(val entry: Entry.Local) : FoodEatenListIntent
}