package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.shared.database.DatabaseKey
import dev.icerock.moko.resources.StringResource

data class SeedMeasurementUnit(
    // TODO: Test uniqueness
    val key: DatabaseKey.MeasurementUnit,
    val name: StringResource,
    val abbreviation: StringResource,
    val factor: Double,
)