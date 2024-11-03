package com.faltenreich.diaguard.export.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory

data class ExportFormMeasurementCategory(
    val category: MeasurementCategory,
    val isExported: Boolean,
    val isMerged: Boolean,
)