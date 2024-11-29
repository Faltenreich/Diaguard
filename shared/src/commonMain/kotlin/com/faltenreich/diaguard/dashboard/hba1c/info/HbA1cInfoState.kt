package com.faltenreich.diaguard.dashboard.hba1c.info

import com.faltenreich.diaguard.measurement.value.MeasurementValue

data class HbA1cInfoState(
    val latest: MeasurementValue.Local?,
    val average: MeasurementValue.Average?,
)