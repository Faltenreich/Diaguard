package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.shared.database.DatabaseKey
import dev.icerock.moko.resources.StringResource

data class SeedMeasurementType(
    // TODO: Test uniqueness
    val key: DatabaseKey.MeasurementType,
    val name: StringResource,
    val minimumValue: Double,
    val lowValue: Double?,
    val highValue: Double?,
    val maximumValue: Double,
    val units: List<SeedMeasurementUnit>,
) {

    constructor(
        key: DatabaseKey.MeasurementType,
        name: StringResource,
        minimumValue: Double,
        lowValue: Double?,
        highValue: Double?,
        maximumValue: Double,
        unit: SeedMeasurementUnit,
    ) : this(key, name, minimumValue, lowValue, highValue, maximumValue, listOf(unit))
}