package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputState
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.tag.Tag

sealed interface EntryFormIntent {

    data class SetDate(val date: Date) : EntryFormIntent

    data class SetTime(val time: Time) : EntryFormIntent

    data class SetNote(val note: String) : EntryFormIntent

    data class SetTagQuery(val tagQuery: String) : EntryFormIntent

    data object OpenReminderPicker : EntryFormIntent

    data object CloseReminderPicker : EntryFormIntent

    data class SetReminder(val delayInMinutes: Int?) : EntryFormIntent

    data object RequestNotificationPermission : EntryFormIntent

    data class Edit(val data: MeasurementPropertyInputState) : EntryFormIntent

    data object Submit : EntryFormIntent

    data class Delete(val needsConfirmation: Boolean) : EntryFormIntent

    data object CloseDeleteDialog : EntryFormIntent

    data object SelectFood : EntryFormIntent

    data class AddFood(val food: Food.Local) : EntryFormIntent

    data class EditFood(val food: FoodEatenInputState) : EntryFormIntent

    data class RemoveFood(val food: FoodEatenInputState) : EntryFormIntent

    data class AddTag(val tag: Tag) : EntryFormIntent

    data class RemoveTag(val tag: Tag) : EntryFormIntent
}