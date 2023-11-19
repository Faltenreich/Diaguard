package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.shared.database.DatabaseKey
import dev.icerock.moko.resources.StringResource

data class SeedMeasurementProperty(
    val key: DatabaseKey.MeasurementProperty,
    val name: StringResource,
    val icon: String,
    val types: List<SeedMeasurementType>,
) {

    constructor(
        key: DatabaseKey.MeasurementProperty,
        name: StringResource,
        icon: String,
        type: SeedMeasurementType,
    ) : this(key, name, icon, listOf(type))
}
