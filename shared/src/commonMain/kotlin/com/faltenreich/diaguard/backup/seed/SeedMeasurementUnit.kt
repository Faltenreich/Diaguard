package com.faltenreich.diaguard.backup.seed

import dev.icerock.moko.resources.StringResource

enum class SeedMeasurementUnit(
    val key: String,
    val localization: StringResource,
    val abbreviation: StringResource,
    val factor: Double,
)