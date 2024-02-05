package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputData
import com.faltenreich.diaguard.food.eaten.FoodEatenInputData
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.tag.Tag

data class EntryFormInput(
    val id: Long?,
    val dateTime: DateTime,
    val measurements: List<MeasurementPropertyInputData>,
    val tags: List<Tag>,
    val note: String?,
    val foodEaten: List<FoodEatenInputData>,
)