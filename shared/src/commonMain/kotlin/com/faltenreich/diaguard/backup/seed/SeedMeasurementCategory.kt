package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.shared.database.DatabaseKey
import org.jetbrains.compose.resources.StringResource

data class SeedMeasurementCategory(
    val key: DatabaseKey.MeasurementCategory,
    val name: StringResource,
    val icon: String,
    val types: List<SeedMeasurementType>,
) {

    constructor(
        key: DatabaseKey.MeasurementCategory,
        name: StringResource,
        icon: String,
        type: SeedMeasurementType,
    ) : this(key, name, icon, listOf(type))
}
