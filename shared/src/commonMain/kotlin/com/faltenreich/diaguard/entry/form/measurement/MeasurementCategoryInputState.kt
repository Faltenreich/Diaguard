package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.category.MeasurementCategory

data class MeasurementCategoryInputState(
    val category: MeasurementCategory,
    val propertyInputStates: List<MeasurementPropertyInputState>,
    val error: String?,
)