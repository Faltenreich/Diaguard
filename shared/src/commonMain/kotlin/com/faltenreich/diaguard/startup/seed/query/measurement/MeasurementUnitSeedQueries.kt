package com.faltenreich.diaguard.startup.seed.query.measurement

import com.faltenreich.diaguard.startup.seed.query.SeedQueries
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.beats_per_minute
import diaguard.shared.generated.resources.beats_per_minute_abbreviation
import diaguard.shared.generated.resources.bread_units
import diaguard.shared.generated.resources.bread_units_abbreviation
import diaguard.shared.generated.resources.carbohydrate_units
import diaguard.shared.generated.resources.carbohydrate_units_abbreviation
import diaguard.shared.generated.resources.carbohydrates
import diaguard.shared.generated.resources.carbohydrates_abbreviation
import diaguard.shared.generated.resources.insulin_units
import diaguard.shared.generated.resources.insulin_units_abbreviation
import diaguard.shared.generated.resources.kilograms
import diaguard.shared.generated.resources.kilograms_abbreviation
import diaguard.shared.generated.resources.milligrams_per_deciliter
import diaguard.shared.generated.resources.milligrams_per_deciliter_abbreviation
import diaguard.shared.generated.resources.millimeters_of_mercury
import diaguard.shared.generated.resources.millimeters_of_mercury_abbreviation
import diaguard.shared.generated.resources.millimoles_per_liter
import diaguard.shared.generated.resources.millimoles_per_liter_abbreviation
import diaguard.shared.generated.resources.millimoles_per_mole
import diaguard.shared.generated.resources.millimoles_per_mole_abbreviation
import diaguard.shared.generated.resources.minutes
import diaguard.shared.generated.resources.minutes_abbreviation
import diaguard.shared.generated.resources.percent
import diaguard.shared.generated.resources.percent_abbreviation
import diaguard.shared.generated.resources.pounds
import diaguard.shared.generated.resources.pounds_abbreviation

class MeasurementUnitSeedQueries(
    private val localization: Localization,
) : SeedQueries<MeasurementUnit.Seed> {

    override fun getAll(): List<MeasurementUnit.Seed> {
        return listOf(
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.BEATS_PER_MINUTE,
                name = localization.getString(Res.string.beats_per_minute),
                abbreviation = localization.getString(Res.string.beats_per_minute_abbreviation),
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.BREAD_UNITS,
                name = localization.getString(Res.string.bread_units),
                abbreviation = localization.getString(Res.string.bread_units_abbreviation),
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.CARBOHYDRATES,
                name = localization.getString(Res.string.carbohydrates),
                abbreviation = localization.getString(Res.string.carbohydrates_abbreviation),
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.CARBOHYDRATE_UNITS,
                name = localization.getString(Res.string.carbohydrate_units),
                abbreviation = localization.getString(Res.string.carbohydrate_units_abbreviation),
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.INSULIN_UNITS,
                name = localization.getString(Res.string.insulin_units),
                abbreviation = localization.getString(Res.string.insulin_units_abbreviation),
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.KILOGRAMS,
                name = localization.getString(Res.string.kilograms),
                abbreviation = localization.getString(Res.string.kilograms_abbreviation),
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.MILLIGRAMS_PER_DECILITER,
                name = localization.getString(Res.string.milligrams_per_deciliter),
                abbreviation = localization.getString(Res.string.milligrams_per_deciliter_abbreviation),
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.MILLIMETERS_OF_MERCURY,
                name = localization.getString(Res.string.millimeters_of_mercury),
                abbreviation = localization.getString(Res.string.millimeters_of_mercury_abbreviation),
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.MILLIMOLES_PER_LITER,
                name = localization.getString(Res.string.millimoles_per_liter),
                abbreviation = localization.getString(Res.string.millimoles_per_liter_abbreviation),
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.MILLIMOLES_PER_MOLE,
                name = localization.getString(Res.string.millimoles_per_mole),
                abbreviation = localization.getString(Res.string.millimoles_per_mole_abbreviation),
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.MINUTES,
                name = localization.getString(Res.string.minutes),
                abbreviation = localization.getString(Res.string.minutes_abbreviation),
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.PERCENT,
                name = localization.getString(Res.string.percent),
                abbreviation = localization.getString(Res.string.percent_abbreviation),
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.POUNDS,
                name = localization.getString(Res.string.pounds),
                abbreviation = localization.getString(Res.string.pounds_abbreviation),
            ),
        )
    }
}