package com.faltenreich.diaguard.data.seed.query.measurement

import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.property.MeasurementValueRange
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.blood_sugar

class BloodSugarSeedQueries(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.BLOOD_SUGAR,
            name = localization.getString(Res.string.blood_sugar),
            icon = "\uD83E\uDE78",
            sortIndex = 0,
            isActive = true,
            properties = listOf(
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                    name = localization.getString(Res.string.blood_sugar),
                    sortIndex = 0,
                    aggregationStyle = MeasurementAggregationStyle.AVERAGE,
                    range = MeasurementValueRange(
                        minimum = 1.0,
                        low = 60.0,
                        target = 120.0,
                        high = 180.0,
                        maximum = 1000.0,
                        isHighlighted = true,
                    ),
                    unitSuggestions = listOf(
                        MeasurementUnitSuggestion.Seed(
                            factor = MeasurementUnitSuggestion.FACTOR_DEFAULT,
                            unit = DatabaseKey.MeasurementUnit.MILLIGRAMS_PER_DECILITER,
                        ),
                        MeasurementUnitSuggestion.Seed(
                            factor = 0.0555,
                            unit = DatabaseKey.MeasurementUnit.MILLIMOLES_PER_LITER,
                        ),
                    ),
                )
            ),
        )
    }
}