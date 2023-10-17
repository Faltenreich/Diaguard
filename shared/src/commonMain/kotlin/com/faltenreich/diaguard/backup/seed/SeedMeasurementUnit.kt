package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.StringResource

enum class SeedMeasurementUnit(
    // TODO: Test uniqueness
    val key: String,
    val localization: StringResource,
    val abbreviation: StringResource,
) {
    BEATS_PER_MINUTE(
        key = "beats_per_minute",
        localization = MR.strings.beats_per_minute,
        abbreviation = MR.strings.beats_per_minute_abbreviation,
    ),
    BREAD_UNITS(
        key = "bread_units",
        localization = MR.strings.bread_units,
        abbreviation = MR.strings.bread_units_abbreviation,
    ),
    CARBOHYDRATES(
        key = "carbohydrates",
        localization = MR.strings.carbohydrates,
        abbreviation = MR.strings.carbohydrates_abbreviation,
    ),
    CARBOHYDRATE_UNITS(
        key = "carbohydrate_units",
        localization = MR.strings.carbohydrate_units,
        abbreviation = MR.strings.carbohydrate_units_abbreviation,
    ),
    INSULIN_UNITS(
        key = "insulin_units",
        localization = MR.strings.insulin_units,
        abbreviation = MR.strings.insulin_units_abbreviation,
    ),
    KILOGRAMS(
        key = "kilograms",
        localization = MR.strings.kilograms,
        abbreviation = MR.strings.kilograms_abbreviation,
    ),
    MILLIGRAMS_PER_DECILITER(
        key = "mg_per_dl",
        localization = MR.strings.milligrams_per_deciliter,
        abbreviation = MR.strings.milligrams_per_deciliter_abbreviation,
    ),
    MILLIMETERS_OF_MERCURY(
        key = "millimeters_of_mercury",
        localization = MR.strings.millimeters_of_mercury,
        abbreviation = MR.strings.millimeters_of_mercury_abbreviation,
    ),
    MILLIMOLES_PER_LITER(
        key = "millimoles_per_liter",
        localization = MR.strings.millimoles_per_liter,
        abbreviation = MR.strings.millimoles_per_liter_abbreviation,
    ),
    MINUTES(
        key = "minutes",
        localization = MR.strings.minutes,
        abbreviation = MR.strings.minutes_abbreviation,
    ),
    PERCENT(
        key = "percent",
        localization = MR.strings.percent,
        abbreviation = MR.strings.percent_abbreviation,
    ),
    POUNDS(
        key = "pounds",
        localization = MR.strings.pounds,
        abbreviation = MR.strings.pounds_abbreviation,
    ),
}