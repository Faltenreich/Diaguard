package com.faltenreich.diaguard.backup.seed

import dev.icerock.moko.resources.StringResource

data class SeedMeasurementType(
    // TODO: Test uniqueness
    val key: String,
    val name: StringResource,
    val units: List<SeedMeasurementUnit>,
) {

    class Builder {

        lateinit var key: String
        lateinit var name: StringResource
        private val units = mutableListOf<SeedMeasurementUnit>()

        fun unit(init: SeedMeasurementUnit.Builder.() -> Unit) {
            units += SeedMeasurementUnit.Builder().apply(init).build()
        }

        fun build(): SeedMeasurementType {
            return SeedMeasurementType(
                key = key,
                name = name,
                units = units,
            )
        }
    }
}