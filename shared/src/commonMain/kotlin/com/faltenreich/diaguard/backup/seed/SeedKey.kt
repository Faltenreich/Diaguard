package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.StringResource

sealed interface SeedKey {

    // TODO: Test uniqueness
    val key: String

    enum class MeasurementProperty(
        override val key: String,
        val localization: StringResource,
        val icon: String,
    ) : SeedKey {
        BLOOD_SUGAR(
            key = "blood_sugar",
            localization = MR.strings.blood_sugar,
            icon = "\uD83E\uDE78",
        ),
        INSULIN(
            key = "insulin",
            localization = MR.strings.insulin,
            icon = "\uD83D\uDC89",
        ),
        MEAL(
            key = "meal",
            localization = MR.strings.meal,
            icon = "\uD83C\uDF5E",
        ),
        ACTIVITY(
            key = "activity",
            localization = MR.strings.activity,
            icon = "\uD83C\uDFC3",
        ),
        HBA1C(
            key = "hba1c",
            localization = MR.strings.hba1c,
            icon = "%",
        ),
        WEIGHT(
            key = "weight",
            localization = MR.strings.weight,
            icon = "\uD83C\uDFCB",
        ),
        PULSE(
            key = "pulse",
            localization = MR.strings.pulse,
            icon = "\uD83D\uDC9A",
        ),
        BLOOD_PRESSURE(
            key = "blood_pressure",
            localization = MR.strings.blood_pressure,
            icon = "⛽",
        ),
        OXYGEN_SATURATION(
            key = "oxygen_saturation",
            localization = MR.strings.oxygen_saturation,
            icon = "O²",
        ),
    }

    enum class MeasurementType(
        override val key: String,
    ) : SeedKey {

    }
}