package com.faltenreich.diaguard.backup.seed

import dev.icerock.moko.resources.StringResource

enum class SeedMeasurementType(
    val key: String,
    val localization: StringResource,
    val units: List<SeedMeasurementUnit> = emptyList(),
)