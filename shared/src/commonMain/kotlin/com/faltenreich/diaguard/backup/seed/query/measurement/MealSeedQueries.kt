package com.faltenreich.diaguard.backup.seed.query.measurement

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.meal

class MealSeedQueries(
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
                    unitSuggestions = listOf(
                        MeasurementUnitSuggestion.Seed(
                            factor = MeasurementUnitSuggestion.FACTOR_DEFAULT,
                            unit = DatabaseKey.MeasurementUnit.CARBOHYDRATES,
                            isDefault = true,
                        ),
                        MeasurementUnitSuggestion.Seed(
                            factor = 0.1,
                            unit = DatabaseKey.MeasurementUnit.CARBOHYDRATE_UNITS,
                            isDefault = false,
                        ),
                        MeasurementUnitSuggestion.Seed(
                            factor = 0.0833,
                            unit = DatabaseKey.MeasurementUnit.BREAD_UNITS,
                            isDefault = false,
                        ),
                    ),
                ),
            ),
        )
    }
}