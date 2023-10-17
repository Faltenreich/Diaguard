package com.faltenreich.diaguard.backup.seed

import kotlinx.serialization.Serializable

@Serializable
data class SeedMeasurementUnit(
    val key: String,
    val name: SeedLocalization,
    val abbreviation: SeedLocalization,
    val factor: Double,
)