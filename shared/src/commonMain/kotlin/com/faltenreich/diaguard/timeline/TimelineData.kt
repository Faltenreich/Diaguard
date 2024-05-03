package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.value.MeasurementValue

data class TimelineData(
    val chart: Chart,
    val table: Table,
) {

    data class Chart(
        val values: List<MeasurementValue>,
    )

    data class Table(
        val categories: List<MeasurementCategory>,
        val values: List<MeasurementValue>,
    )
}