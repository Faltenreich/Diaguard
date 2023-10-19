package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.shared.database.DatabaseKey
import dev.icerock.moko.resources.StringResource

data class SeedMeasurementType(
    // TODO: Test uniqueness
    val key: DatabaseKey.MeasurementType,
    val name: StringResource,
    val units: List<SeedMeasurementUnit>,
) {

    constructor(
        key: DatabaseKey.MeasurementType,
        name: StringResource,
        unit: SeedMeasurementUnit,
    ) : this(key, name, listOf(unit))
}