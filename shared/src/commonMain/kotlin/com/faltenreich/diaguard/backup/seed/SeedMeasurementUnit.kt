package com.faltenreich.diaguard.backup.seed

import dev.icerock.moko.resources.StringResource
import kotlin.properties.Delegates

data class SeedMeasurementUnit(
    // TODO: Test uniqueness
    val key: String,
    val name: StringResource,
    val abbreviation: StringResource,
    val factor: Double,
) {

    class Builder {

        lateinit var key: String
        lateinit var name: StringResource
        lateinit var abbreviation: StringResource
        var factor by Delegates.notNull<Double>()

        fun build(): SeedMeasurementUnit {
            return SeedMeasurementUnit(
                key = key,
                name = name,
                abbreviation = abbreviation,
                factor = factor,
            )
        }
    }
}