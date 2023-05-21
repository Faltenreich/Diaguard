package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.entry.EntryDto
import com.faltenreich.diaguard.measurement.type.MeasurementTypeDto

data class MeasurementValueDto(
    val entity: MeasurementValue,
    val type: MeasurementTypeDto,
    val entry: EntryDto,
)