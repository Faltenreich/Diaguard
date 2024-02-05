package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData

data class MeasurementValueIsTooLowException(val input: MeasurementTypeInputData) : Exception()