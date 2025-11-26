package com.faltenreich.diaguard.data.seed.query.measurement

import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.property.MeasurementValueRange
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.oxygen_saturation

class OxygenSaturationSeedQueries(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.OXYGEN_SATURATION,
            name = localization.getString(Res.string.oxygen_saturation),
            icon = "☁\uFE0F",
            sortIndex = 8,
            isActive = true,
            properties = listOf(
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.OXYGEN_SATURATION,
                    name = localization.getString(Res.string.oxygen_saturation),
                    sortIndex = 0,
                    aggregationStyle = MeasurementAggregationStyle.AVERAGE,
                    range = MeasurementValueRange(
                        minimum = 50.0,
                        low = 90.0,
                        target = 97.0,
                        high = 100.0,
                        maximum = 100.0,
                        isHighlighted = true,
                    ),
                    unitSuggestions = listOf(
                        MeasurementUnitSuggestion.Seed(
                            factor = MeasurementUnitSuggestion.FACTOR_DEFAULT,
                            unit = DatabaseKey.MeasurementUnit.PERCENT,
                        ),
                    ),
                ),
            ),
        )
    }
}