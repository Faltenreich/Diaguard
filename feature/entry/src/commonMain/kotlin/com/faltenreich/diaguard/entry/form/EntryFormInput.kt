package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.entry.form.food.FoodEatenInputState
import com.faltenreich.diaguard.entry.form.measurement.MeasurementCategoryInputState

data class EntryFormInput(
    val entry: Entry.Local?,
    val dateTime: DateTime,
    val measurements: List<MeasurementCategoryInputState>,
    val tags: Collection<Tag>,
    val note: String?,
    val foodEaten: List<FoodEatenInputState>,
)