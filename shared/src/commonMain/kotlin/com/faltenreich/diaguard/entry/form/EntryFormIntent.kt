package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.input.FoodInputData

sealed interface EntryFormIntent {

    data class Edit(val data: MeasurementTypeInputData) : EntryFormIntent

    data object Submit : EntryFormIntent

    data object Delete : EntryFormIntent

    data class AddFood(val food: Food) : EntryFormIntent

    data class RemoveFood(val food: FoodInputData) : EntryFormIntent
}