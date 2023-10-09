package com.faltenreich.diaguard.backup.seed

import kotlinx.serialization.Serializable

@Serializable
data class SeedMeasurementUnit(
    val factor: Double,
    val name: List<SeedLocalization>,
    val abbreviation: List<SeedLocalization>,
)