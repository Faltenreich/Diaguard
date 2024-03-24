package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputState
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.tag.Tag

sealed interface EntryFormIntent {

    data class Edit(val data: MeasurementTypeInputState) : EntryFormIntent

    data object SelectDate : EntryFormIntent

    data object SelectTime : EntryFormIntent

    data object Submit : EntryFormIntent

    data object Delete : EntryFormIntent

    data object SelectFood : EntryFormIntent

    data class AddFood(val food: Food) : EntryFormIntent

    data class EditFood(val food: FoodEatenInputState) : EntryFormIntent

    data class RemoveFood(val food: FoodEatenInputState) : EntryFormIntent

    data class AddTag(val tag: Tag) : EntryFormIntent

    data class RemoveTag(val tag: Tag) : EntryFormIntent
}