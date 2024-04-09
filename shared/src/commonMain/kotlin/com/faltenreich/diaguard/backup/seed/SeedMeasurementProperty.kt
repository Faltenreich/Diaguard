package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import org.jetbrains.compose.resources.StringResource

data class SeedMeasurementProperty(
    // TODO: Test uniqueness
    val key: DatabaseKey.MeasurementProperty,
    val name: StringResource,
    val range: MeasurementValueRange,
    val units: List<SeedMeasurementUnit>,
)