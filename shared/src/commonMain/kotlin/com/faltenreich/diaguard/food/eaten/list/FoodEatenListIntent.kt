package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.entry.Entry

sealed interface FoodEatenListIntent {

    data class OpenEntry(val entry: Entry) : FoodEatenListIntent
}