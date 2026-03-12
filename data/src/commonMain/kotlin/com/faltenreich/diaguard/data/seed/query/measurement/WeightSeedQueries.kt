package com.faltenreich.diaguard.data.seed.query.measurement

import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.property.MeasurementValueRange
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.weight

class WeightSeedQueries(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.WEIGHT,
            name = localization.getString(Res.string.weight),
            icon = "\uD83C\uDFCB",
            sortIndex = 5,
            isActive = true,
            properties = listOf(
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.WEIGHT,
                    name = localization.getString(Res.string.weight),
                    sortIndex = 0,
                    aggregationStyle = MeasurementAggregationStyle.AVERAGE,
                    range = MeasurementValueRange(
                        minimum = 1.0,
                        low = null,
                        target = null,
                        high = null,
                        maximum = 1400.0,
                        isHighlighted = false,
                    ),
                    unitSuggestions = listOf(
                        MeasurementUnitSuggestion.Seed(
                            factor = MeasurementUnitSuggestion.FACTOR_DEFAULT,
                            unit = DatabaseKey.MeasurementUnit.KILOGRAMS,
                        ),
                        MeasurementUnitSuggestion.Seed(
                            factor = 2.20462262185,
                            unit = DatabaseKey.MeasurementUnit.POUNDS,
                        ),
                    ),
                ),
            ),
        )
    }
}