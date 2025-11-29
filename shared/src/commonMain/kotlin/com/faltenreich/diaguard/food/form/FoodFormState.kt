package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.Food

data class FoodFormState(
    val food: Food.Local?,
    val input: FoodFormInput,
    val error: String?,
    val deleteDialog: DeleteDialog?,
) {

    data object DeleteDialog
}