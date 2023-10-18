package com.faltenreich.diaguard.backup.seed

import dev.icerock.moko.resources.StringResource

data class SeedMeasurementUnit(
    // TODO: Test uniqueness
    val key: String,
    val name: StringResource,
    val abbreviation: StringResource,
    val factor: Double,
)