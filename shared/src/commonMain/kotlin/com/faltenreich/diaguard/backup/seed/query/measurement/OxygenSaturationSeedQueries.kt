package com.faltenreich.diaguard.backup.seed.query.measurement

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.oxygen_saturation

class OxygenSaturationSeedQueries(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.OXYGEN_SATURATION,
            name = localization.getString(Res.string.oxygen_saturation),
            icon = "O²",
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