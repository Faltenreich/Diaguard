package com.faltenreich.diaguard.data.seed.query.measurement

import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.data.seed.query.SeedQueries
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.beats_per_minute
import com.faltenreich.diaguard.resource.beats_per_minute_abbreviation
import com.faltenreich.diaguard.resource.bread_units
import com.faltenreich.diaguard.resource.bread_units_abbreviation
import com.faltenreich.diaguard.resource.carbohydrate_units
import com.faltenreich.diaguard.resource.carbohydrate_units_abbreviation
import com.faltenreich.diaguard.resource.carbohydrates
import com.faltenreich.diaguard.resource.carbohydrates_abbreviation
import com.faltenreich.diaguard.resource.insulin_units
import com.faltenreich.diaguard.resource.insulin_units_abbreviation
import com.faltenreich.diaguard.resource.kilograms
import com.faltenreich.diaguard.resource.kilograms_abbreviation
import com.faltenreich.diaguard.resource.milligrams_per_deciliter
import com.faltenreich.diaguard.resource.milligrams_per_deciliter_abbreviation
import com.faltenreich.diaguard.resource.millimeters_of_mercury
import com.faltenreich.diaguard.resource.millimeters_of_mercury_abbreviation
import com.faltenreich.diaguard.resource.millimoles_per_liter
import com.faltenreich.diaguard.resource.millimoles_per_liter_abbreviation
import com.faltenreich.diaguard.resource.millimoles_per_mole
import com.faltenreich.diaguard.resource.millimoles_per_mole_abbreviation
import com.faltenreich.diaguard.resource.minutes
import com.faltenreich.diaguard.resource.minutes_abbreviation
import com.faltenreich.diaguard.resource.percent
import com.faltenreich.diaguard.resource.percent_abbreviation
import com.faltenreich.diaguard.resource.pounds
import com.faltenreich.diaguard.resource.pounds_abbreviation

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