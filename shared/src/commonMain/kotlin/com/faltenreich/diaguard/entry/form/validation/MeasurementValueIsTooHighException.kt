package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData

data class MeasurementValueIsTooHighException(val input: MeasurementTypeInputData) : Exception()