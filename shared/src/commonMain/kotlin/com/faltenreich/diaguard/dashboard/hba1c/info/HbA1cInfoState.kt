package com.faltenreich.diaguard.dashboard.hba1c.info

import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint

data class HbA1cInfoState(
    val latest: Latest?,
    val estimated: Estimated?,
) {

    data class Latest(
        val value: String,
        val dateTime: String,
        val tint: MeasurementValueTint,
    )

    data class Estimated(
        val value: String,
        val dateRange: String,
        val valueCount: Int,
        val tint: MeasurementValueTint,
    )
}