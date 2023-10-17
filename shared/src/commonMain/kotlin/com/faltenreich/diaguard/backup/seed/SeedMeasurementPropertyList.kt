package com.faltenreich.diaguard.backup.seed

data class SeedMeasurementPropertyList(
    val properties: List<SeedMeasurementProperty>,
) {

    class Builder {

        private val properties = mutableListOf<SeedMeasurementProperty>()

        fun property(init: SeedMeasurementProperty.Builder.() -> Unit) {
            properties += SeedMeasurementProperty.Builder().apply(init).build()
        }

        fun build(): SeedMeasurementPropertyList {
            return SeedMeasurementPropertyList(properties)
        }
    }
}

fun properties(init: SeedMeasurementPropertyList.Builder.() -> Unit): List<SeedMeasurementProperty> {
    return SeedMeasurementPropertyList.Builder().apply(init).build().properties
}