package com.faltenreich.diaguard.import.seed

import kotlinx.serialization.Serializable

@Serializable
data class SeedMeasurementType(
    val name: List<SeedLocalization>,
    val units: List<SeedMeasurementUnit>,
)