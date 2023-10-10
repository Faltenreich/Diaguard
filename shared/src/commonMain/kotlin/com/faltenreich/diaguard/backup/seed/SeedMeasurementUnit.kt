package com.faltenreich.diaguard.backup.seed

import kotlinx.serialization.Serializable

@Serializable
data class SeedMeasurementUnit(
    val factor: Double,
    val name: SeedLocalization,
    val abbreviation: SeedLocalization,
)