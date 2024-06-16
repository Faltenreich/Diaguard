package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.bread_units
import diaguard.shared.generated.resources.bread_units_abbreviation
import diaguard.shared.generated.resources.carbohydrate_units
import diaguard.shared.generated.resources.carbohydrate_units_abbreviation
import diaguard.shared.generated.resources.carbohydrates
import diaguard.shared.generated.resources.carbohydrates_abbreviation
import diaguard.shared.generated.resources.meal

class MealSeed(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.MEAL,
            name = localization.getString(Res.string.meal),
            icon = "\uD83C\uDF5E",
            sortIndex = 2,
            isActive = true,
            properties = listOf(
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.MEAL,
                    name = localization.getString(Res.string.meal),
                    sortIndex = 0,
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
                    ),
                ),
            ),
        )
    }
}