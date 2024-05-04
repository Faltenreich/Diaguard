package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.backup.seed.SeedMeasurementCategory
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.bread_units
import diaguard.shared.generated.resources.bread_units_abbreviation
import diaguard.shared.generated.resources.carbohydrate_units
import diaguard.shared.generated.resources.carbohydrate_units_abbreviation
import diaguard.shared.generated.resources.carbohydrates
import diaguard.shared.generated.resources.carbohydrates_abbreviation
import diaguard.shared.generated.resources.meal

class MealSeed {

    operator fun invoke(): SeedMeasurementCategory {
        return SeedMeasurementCategory(
            key = DatabaseKey.MeasurementCategory.MEAL,
            name = Res.string.meal,
            icon = "\uD83C\uDF5E",
            property = SeedMeasurementProperty(
                key = DatabaseKey.MeasurementProperty.MEAL,
                name = Res.string.meal,
                aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
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