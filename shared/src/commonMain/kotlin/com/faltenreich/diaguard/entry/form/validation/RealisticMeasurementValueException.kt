package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData

data class RealisticMeasurementValueException(
    val violations: List<MeasurementTypeInputData>,
) : Exception()