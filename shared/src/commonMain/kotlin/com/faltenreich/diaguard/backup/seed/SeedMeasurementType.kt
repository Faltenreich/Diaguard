package com.faltenreich.diaguard.backup.seed

import kotlinx.serialization.Serializable

@Serializable
data class SeedMeasurementType(
    val key: String,
    val name: SeedLocalization,
    val units: List<SeedMeasurementUnit>,
)