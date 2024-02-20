package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputState

data class RealisticMeasurementValueException(
    val violations: List<MeasurementTypeInputState>,
) : Exception()