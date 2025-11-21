package com.faltenreich.diaguard.startup.seed.query.measurement

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.data.measurement.property.MeasurementValueRange
import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.pulse

class PulseSeedQueries(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.PULSE,
            name = localization.getString(Res.string.pulse),
            icon = "\uD83D\uDC9A",
            sortIndex = 6,
            isActive = true,
            properties = listOf(
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.PULSE,
                    name = localization.getString(Res.string.pulse),
                    sortIndex = 0,
                    aggregationStyle = MeasurementAggregationStyle.AVERAGE,
                    range = MeasurementValueRange(
                        minimum = 1.0,
                        low = 60.0,
                        target = 70.0,
                        high = 80.0,
                        maximum = 200.0,
                        isHighlighted = true,
                    ),
                    unitSuggestions = listOf(
                        MeasurementUnitSuggestion.Seed(
                            factor = MeasurementUnitSuggestion.FACTOR_DEFAULT,
                            unit = DatabaseKey.MeasurementUnit.BEATS_PER_MINUTE,
                        ),
                    ),
                ),
            ),
        )
    }
}