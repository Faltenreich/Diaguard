package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class MeasurementPropertyInputState(
    val property: MeasurementProperty.Local,
    // FIXME: Sometimes updated seemingly asynchronously, e.g. when entering multiple numbers into EntryForm
    val input: String,
    val isLast: Boolean,
    val error: String?,
    val decimalPlaces: Int,
)