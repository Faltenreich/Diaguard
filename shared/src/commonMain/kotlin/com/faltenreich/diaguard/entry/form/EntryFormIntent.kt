package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEatenInputData
import com.faltenreich.diaguard.tag.Tag

sealed interface EntryFormIntent {

    data class Edit(val data: MeasurementTypeInputData) : EntryFormIntent

    data object Submit : EntryFormIntent

    data object Delete : EntryFormIntent

    data object SelectFood : EntryFormIntent

    data class AddFood(val food: Food) : EntryFormIntent

    data class EditFood(val food: FoodEatenInputData) : EntryFormIntent

    data class RemoveFood(val food: FoodEatenInputData) : EntryFormIntent

    data class AddTag(val tag: Tag) : EntryFormIntent

    data class RemoveTag(val tag: Tag) : EntryFormIntent
}