package com.faltenreich.diaguard.backup.seed.data

import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.backup.seed.SeedMeasurementCategory
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey

class MealSeed {

    operator fun invoke(): SeedMeasurementCategory {
        return SeedMeasurementCategory(
            key = DatabaseKey.MeasurementCategory.MEAL,
            name = Res.string.meal,
            icon = "\uD83C\uDF5E",
            type = SeedMeasurementType(
                key = DatabaseKey.MeasurementType.MEAL,
                name = Res.string.meal,
                range = MeasurementValueRange(
                    minimum = 0.0,
                    low = null,
                    target = null,
                    high = null,
                    maximum = 1000.0,
                    isHighlighted = false,
                ),
                units = listOf(
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.MEAL_CARBOHYDRATES,
                        name = Res.string.carbohydrates,
                        abbreviation = Res.string.carbohydrates_abbreviation,
                        factor = 1.0,
                    ),
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.MEAL_CARBOHYDRATE_UNITS,
                        name = Res.string.carbohydrate_units,
                        abbreviation = Res.string.carbohydrate_units_abbreviation,
                        factor = 0.1,
                    ),
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.MEAL_BREAD_UNITS,
                        name = Res.string.bread_units,
                        abbreviation = Res.string.bread_units_abbreviation,
                        factor = 0.0833,
                    ),
                ),
            ),
        )
    }
}