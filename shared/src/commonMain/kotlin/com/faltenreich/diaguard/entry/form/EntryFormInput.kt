package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputState
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.tag.Tag

data class EntryFormInput(
    val id: Long?,
    val dateTime: DateTime,
    val measurements: List<MeasurementPropertyInputState>,
    val tags: List<Tag>,
    val note: String?,
    val foodEaten: List<FoodEatenInputState>,
)