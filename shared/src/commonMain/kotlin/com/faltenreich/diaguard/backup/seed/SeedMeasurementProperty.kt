package com.faltenreich.diaguard.backup.seed

import dev.icerock.moko.resources.StringResource

data class SeedMeasurementProperty(
    val key: String,
    val name: StringResource,
    val icon: String,
    val types: List<SeedMeasurementType>,
) {

    class Builder {

        lateinit var key: String
        lateinit var name: StringResource
        lateinit var icon: String
        private val types = mutableListOf<SeedMeasurementType>()

        fun type(init: SeedMeasurementType.Builder.() -> Unit) {
            types += SeedMeasurementType.Builder().apply(init).build()
        }

        fun build(): SeedMeasurementProperty {
            return SeedMeasurementProperty(
                key = key,
                name = name,
                icon = icon,
                types = types,
            )
        }
    }
}