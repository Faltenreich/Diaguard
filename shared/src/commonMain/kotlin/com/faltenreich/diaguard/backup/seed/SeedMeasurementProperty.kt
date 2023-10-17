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

/*
enum class SeedMeasurementProperty(
    // TODO: Test uniqueness
    val key: String,
    val localization: StringResource,
    val icon: String,
    val types: List<SeedMeasurementType>,
) {
    BLOOD_SUGAR(
        key = "blood_sugar",
        localization = MR.strings.blood_sugar,
        icon = "\uD83E\uDE78",
        types = listOf(
            SeedMeasurementType.BLOOD_SUGAR,
        ),
    ),
    INSULIN(
        key = "insulin",
        localization = MR.strings.insulin,
        icon = "\uD83D\uDC89",
        types = listOf(
            SeedMeasurementType.BOLUS,
            SeedMeasurementType.CORRECTION,
            SeedMeasurementType.BASAL,
        ),
    ),
    MEAL(
        key = "meal",
        localization = MR.strings.meal,
        icon = "\uD83C\uDF5E",
        types = listOf(
            SeedMeasurementType.MEAL,
        ),
    ),
    ACTIVITY(
        key = "activity",
        localization = MR.strings.activity,
        icon = "\uD83C\uDFC3",
        types = listOf(
            SeedMeasurementType.ACTIVITY,
        ),
    ),
    HBA1C(
        key = "hba1c",
        localization = MR.strings.hba1c,
        icon = "%",
        types = listOf(
            SeedMeasurementType.HBA1C,
        ),
    ),
    WEIGHT(
        key = "weight",
        localization = MR.strings.weight,
        icon = "\uD83C\uDFCB",
        types = listOf(
            SeedMeasurementType.WEIGHT,
        ),
    ),
    PULSE(
        key = "pulse",
        localization = MR.strings.pulse,
        icon = "\uD83D\uDC9A",
        types = listOf(
            SeedMeasurementType.PULSE,
        ),
    ),
    BLOOD_PRESSURE(
        key = "blood_pressure",
        localization = MR.strings.blood_pressure,
        icon = "⛽",
        types = listOf(
            SeedMeasurementType.SYSTOLIC,
            SeedMeasurementType.DIASTOLIC,
        ),
    ),
    OXYGEN_SATURATION(
        key = "oxygen_saturation",
        localization = MR.strings.oxygen_saturation,
        icon = "O²",
        types = listOf(
            SeedMeasurementType.OXYGEN_SATURATION,
        ),
    ),
}
*/