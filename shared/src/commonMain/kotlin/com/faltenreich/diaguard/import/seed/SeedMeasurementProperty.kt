package com.faltenreich.diaguard.import.seed

import kotlinx.serialization.Serializable

@Serializable
data class SeedMeasurementProperty(
    val name: List<SeedLocalization>,
    val types: List<SeedMeasurementType>,
)