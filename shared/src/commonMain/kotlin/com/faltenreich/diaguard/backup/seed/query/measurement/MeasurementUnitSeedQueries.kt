package com.faltenreich.diaguard.backup.seed.query.measurement

import com.faltenreich.diaguard.backup.seed.query.SeedQueries
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
                key = DatabaseKey.MeasurementUnit.ACTIVITY,
                name = localization.getString(Res.string.minutes),
                abbreviation = localization.getString(Res.string.minutes_abbreviation),
                factor = 1.0,
                isSelected = true,
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.BLOOD_PRESSURE_SYSTOLIC,
                name = localization.getString(Res.string.millimeters_of_mercury),
                abbreviation = localization.getString(Res.string.millimeters_of_mercury_abbreviation),
                factor = 1.0,
                isSelected = true,
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.BLOOD_PRESSURE_DIASTOLIC,
                name = localization.getString(Res.string.millimeters_of_mercury),
                abbreviation = localization.getString(Res.string.millimeters_of_mercury_abbreviation),
                factor = 1.0,
                isSelected = true,
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.BLOOD_SUGAR_MILLIGRAMS_PER_DECILITER,
                name = localization.getString(Res.string.milligrams_per_deciliter),
                abbreviation = localization.getString(Res.string.milligrams_per_deciliter_abbreviation),
                factor = 1.0,
                isSelected = true,
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.BLOOD_SUGAR_MILLIMOLES_PER_LITER,
                name = localization.getString(Res.string.millimoles_per_liter),
                abbreviation = localization.getString(Res.string.millimoles_per_liter_abbreviation),
                factor = 0.0555,
                isSelected = false,
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.HBA1C_PERCENT,
                name = localization.getString(Res.string.percent),
                abbreviation = localization.getString(Res.string.percent_abbreviation),
                factor = 1.0,
                isSelected = true,
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.HBA1C_MILLIMOLES_PER_MOLES,
                name = localization.getString(Res.string.millimoles_per_mole),
                abbreviation = localization.getString(Res.string.millimoles_per_mole_abbreviation),
                factor = 0.00001,
                isSelected = false,
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.MEAL_CARBOHYDRATES,
                name = localization.getString(Res.string.carbohydrates),
                abbreviation = localization.getString(Res.string.carbohydrates_abbreviation),
                factor = 1.0,
                isSelected = true,
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.MEAL_CARBOHYDRATE_UNITS,
                name = localization.getString(Res.string.carbohydrate_units),
                abbreviation = localization.getString(Res.string.carbohydrate_units_abbreviation),
                factor = 0.1,
                isSelected = false,
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.MEAL_BREAD_UNITS,
                name = localization.getString(Res.string.bread_units),
                abbreviation = localization.getString(Res.string.bread_units_abbreviation),
                factor = 0.0833,
                isSelected = false,
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.OXYGEN_SATURATION,
                name = localization.getString(Res.string.percent),
                abbreviation = localization.getString(Res.string.percent_abbreviation),
                factor = 1.0,
                isSelected = true,
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.PULSE,
                name = localization.getString(Res.string.beats_per_minute),
                abbreviation = localization.getString(Res.string.beats_per_minute_abbreviation),
                factor = 1.0,
                isSelected = true,
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.WEIGHT_KILOGRAMS,
                name = localization.getString(Res.string.kilograms),
                abbreviation = localization.getString(Res.string.kilograms_abbreviation),
                factor = 1.0,
                isSelected = true,
            ),
            MeasurementUnit.Seed(
                key = DatabaseKey.MeasurementUnit.WEIGHT_POUNDS,
                name = localization.getString(Res.string.pounds),
                abbreviation = localization.getString(Res.string.pounds_abbreviation),
                factor = 2.20462262185,
                isSelected = false,
            ),
        )
    }
}