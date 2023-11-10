package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData
import com.faltenreich.diaguard.food.Food

sealed interface EntryFormIntent {

    data class Edit(val data: MeasurementTypeInputData) : EntryFormIntent

    data object Submit : EntryFormIntent

    data object Delete : EntryFormIntent

    data class AddFood(val food: Food) : EntryFormIntent
}