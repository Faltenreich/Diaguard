package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory

data class MeasurementCategoryInputState(
    val category: MeasurementCategory.Local,
    val propertyInputStates: List<MeasurementPropertyInputState>,
) {

    val hasError: Boolean = propertyInputStates.any { it.error != null }
}