package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.category.MeasurementCategory

data class MeasurementCategoryInputState(
    val category: MeasurementCategory,
    val typeInputStates: List<MeasurementTypeInputState>,
    val error: String?,
)