package com.faltenreich.diaguard.backup.seed

import dev.icerock.moko.resources.StringResource

data class SeedMeasurementType(
    // TODO: Test uniqueness
    val key: String,
    val name: StringResource,
    val units: List<SeedMeasurementUnit>,
) {

    constructor(
        key: String,
        name: StringResource,
        unit: SeedMeasurementUnit,
    ) : this(key, name, listOf(unit))
}