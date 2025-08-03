package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.entry.form.measurement.MeasurementCategoryInputState
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.tag.Tag

data class EntryFormState(
    val dateTime: DateTime,
    val note: String,
    val alarmDelayInMinutes: Int?,
    val measurements: List<MeasurementCategoryInputState>,
    val foodEaten: List<FoodEatenInputState>,
    val tags: Tags,
    val deleteDialog: DeleteDialog?,
) {

    val hasError: Boolean = measurements.any { it.hasError }

    data class DateTime(
        val date: Date,
        val dateLocalized: String,
        val time: Time,
        val timeLocalized: String,
    )

    data class Tags(
        val query: String,
        val suggestions: Collection<Tag>,
        val selection: Collection<Tag>,
    )

    data object DeleteDialog
}