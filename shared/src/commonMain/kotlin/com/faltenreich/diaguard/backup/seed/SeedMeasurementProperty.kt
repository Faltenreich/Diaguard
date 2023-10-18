package com.faltenreich.diaguard.backup.seed

import dev.icerock.moko.resources.StringResource

data class SeedMeasurementProperty(
    // TODO: Test uniqueness
    val key: String,
    val name: StringResource,
    val icon: String,
    val types: List<SeedMeasurementType>,
) {

    constructor(
        key: String,
        name: StringResource,
        icon: String,
        type: SeedMeasurementType,
    ) : this(key, name, icon, listOf(type))
}
