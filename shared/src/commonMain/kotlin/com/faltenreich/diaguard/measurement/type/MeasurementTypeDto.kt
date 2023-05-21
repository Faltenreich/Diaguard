package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDto
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

data class MeasurementTypeDto(
    val entity: MeasurementType,
    val property: MeasurementPropertyDto,
    val units: List<MeasurementUnit>,
    val selectedUnit: MeasurementUnit,
)