package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.measurement.value.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import dev.icerock.moko.resources.StringResource

data class SeedMeasurementType(
    // TODO: Test uniqueness
    val key: DatabaseKey.MeasurementType,
    val name: StringResource,
    val range: MeasurementValueRange,
    val units: List<SeedMeasurementUnit>,
)