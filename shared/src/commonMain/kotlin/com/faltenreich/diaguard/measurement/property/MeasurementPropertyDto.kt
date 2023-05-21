package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.measurement.type.MeasurementTypeDto

data class MeasurementPropertyDto(
    val entity: MeasurementProperty,
    val types: List<MeasurementTypeDto>,
)