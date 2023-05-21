package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.measurement.value.MeasurementValueDto

data class EntryDto(
    val entity: Entry,
    val values: List<MeasurementValueDto>,
)