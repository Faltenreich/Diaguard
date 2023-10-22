package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class ExportFormMeasurementProperty(
    val property: MeasurementProperty,
    val isExported: Boolean,
    val isMerged: Boolean,
)