package com.faltenreich.diaguard.backup.seed.property

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.Seed
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseKey

class MealSeed : Seed<SeedMeasurementProperty> {

    override fun harvest(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = DatabaseKey.MeasurementProperty.MEAL,
            name = MR.strings.meal,
            icon = "\uD83C\uDF5E",
            type = SeedMeasurementType(
                key = DatabaseKey.MeasurementType.MEAL,
                name = MR.strings.meal,
                units = listOf(
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.MEAL_CARBOHYDRATES,
                        name = MR.strings.carbohydrates,
                        abbreviation = MR.strings.carbohydrates_abbreviation,
                        factor = 1.0,
                    ),
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.MEAL_CARBOHYDRATE_UNITS,
                        name = MR.strings.carbohydrate_units,
                        abbreviation = MR.strings.carbohydrate_units_abbreviation,
                        factor = 0.1,
                    ),
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.MEAL_BREAD_UNITS,
                        name = MR.strings.bread_units,
                        abbreviation = MR.strings.bread_units_abbreviation,
                        factor = 0.0833,
                    ),
                ),
            ),
        )
    }
}