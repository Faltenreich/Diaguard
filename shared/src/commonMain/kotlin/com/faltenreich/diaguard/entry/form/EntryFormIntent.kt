package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEatenInputData

sealed interface EntryFormIntent {

    data class Edit(val data: MeasurementTypeInputData) : EntryFormIntent

    data object Submit : EntryFormIntent

    data object Delete : EntryFormIntent

    data class AddFood(val food: Food) : EntryFormIntent

    data class EditFood(val food: FoodEatenInputData) : EntryFormIntent

    data class RemoveFood(val food: FoodEatenInputData) : EntryFormIntent
}