package com.faltenreich.diaguard.backup.seed

import kotlinx.serialization.Serializable

@Serializable
data class SeedMeasurementProperty(
    val name: List<SeedLocalization>,
    val types: List<SeedMeasurementType>,
)