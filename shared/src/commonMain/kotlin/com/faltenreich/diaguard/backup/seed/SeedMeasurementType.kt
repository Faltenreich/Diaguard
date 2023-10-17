package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.StringResource

enum class SeedMeasurementType(
    // TODO: Test uniqueness
    val key: String,
    val localization: StringResource,
    val unitsWithFactor: List<Pair<SeedMeasurementUnit, Double>>,
) {
    ACTIVITY(
        key = "activity",
        localization = MR.strings.activity,
        unitsWithFactor = listOf(
            SeedMeasurementUnit.MINUTES to 1.0,
        ),
    ),
    BASAL(
        key = "basal",
        localization = MR.strings.basal,
        unitsWithFactor = listOf(
            SeedMeasurementUnit.INSULIN_UNITS to 1.0,
        ),
    ),
    BLOOD_SUGAR(
        key = "blood_sugar",
        localization = MR.strings.blood_sugar,
        unitsWithFactor = listOf(
            SeedMeasurementUnit.MILLIGRAMS_PER_DECILITER to 1.0,
            SeedMeasurementUnit.MILLIMOLES_PER_LITER to 0.0555,
        ),
    ),
    BOLUS(
        key = "bolus",
        localization = MR.strings.bolus,
        unitsWithFactor = listOf(
            SeedMeasurementUnit.INSULIN_UNITS to 1.0,
        ),
    ),
    CORRECTION(
        key = "correction",
        localization = MR.strings.correction,
        unitsWithFactor = listOf(
            SeedMeasurementUnit.INSULIN_UNITS to 1.0,
        ),
    ),
    DIASTOLIC(
        key = "diastolic",
        localization = MR.strings.diastolic,
        unitsWithFactor = listOf(
            SeedMeasurementUnit.MILLIMETERS_OF_MERCURY to 1.0,
        ),
    ),
    HBA1C(
        key = "hba1c",
        localization = MR.strings.hba1c,
        unitsWithFactor = listOf(
            SeedMeasurementUnit.PERCENT to 1.0,
        ),
    ),
    MEAL(
        key = "meal",
        localization = MR.strings.meal,
        unitsWithFactor = listOf(
            SeedMeasurementUnit.CARBOHYDRATES to 1.0,
            SeedMeasurementUnit.CARBOHYDRATE_UNITS to 0.1,
            SeedMeasurementUnit.BREAD_UNITS to 0.0833,
        ),
    ),
    OXYGEN_SATURATION(
        key = "oxygen_saturation",
        localization = MR.strings.oxygen_saturation,
        unitsWithFactor = listOf(
            SeedMeasurementUnit.PERCENT to 1.0,
        ),
    ),
    PULSE(
        key = "pulse",
        localization = MR.strings.pulse,
        unitsWithFactor = listOf(
            SeedMeasurementUnit.BEATS_PER_MINUTE to 1.0,
        ),
    ),
    SYSTOLIC(
        key = "systolic",
        localization = MR.strings.systolic,
        unitsWithFactor = listOf(
            SeedMeasurementUnit.MILLIMETERS_OF_MERCURY to 1.0,
        ),
    ),
    WEIGHT(
        key = "weight",
        localization = MR.strings.weight,
        unitsWithFactor = listOf(
            SeedMeasurementUnit.KILOGRAMS to 1.0,
            SeedMeasurementUnit.POUNDS to 2.20462262185,
        ),
    ),
}