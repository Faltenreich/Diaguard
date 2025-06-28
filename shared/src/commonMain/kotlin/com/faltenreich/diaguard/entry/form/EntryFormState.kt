package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.form.measurement.MeasurementCategoryInputState
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.tag.Tag

data class EntryFormState(
    val measurements: List<MeasurementCategoryInputState>,
    val foodEaten: List<FoodEatenInputState>,
    val tagSuggestions: List<Tag>,
    val tagSelection: List<Tag>,
    val deleteDialog: DeleteDialog?,
) {

    val hasError: Boolean = measurements.any { it.hasError }

    data object DeleteDialog
}