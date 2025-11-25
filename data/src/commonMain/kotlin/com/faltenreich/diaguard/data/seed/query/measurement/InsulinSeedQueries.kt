package com.faltenreich.diaguard.data.seed.query.measurement

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.data.measurement.property.MeasurementValueRange
import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.localization.Localization
import diaguard.data.generated.resources.Res
import diaguard.data.generated.resources.basal
import diaguard.data.generated.resources.bolus
import diaguard.data.generated.resources.correction
import diaguard.data.generated.resources.insulin

class InsulinSeedQueries(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.INSULIN,
            name = localization.getString(Res.string.insulin),
            icon = "\uD83D\uDC89",
            sortIndex = 1,
            isActive = true,
            properties = listOf(
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.INSULIN_BOLUS,
                    name = localization.getString(Res.string.bolus),
                    sortIndex = 0,
                    aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                    range = MeasurementValueRange(
                        minimum = 0.0,
                        low = null,
                        target = null,
                        high = null,
                        maximum = 100.0,
                        isHighlighted = false,
                    ),
                    unitSuggestions = listOf(
                        MeasurementUnitSuggestion.Seed(
                            factor = MeasurementUnitSuggestion.FACTOR_DEFAULT,
                            unit = DatabaseKey.MeasurementUnit.INSULIN_UNITS,
                        ),
                    ),
                ),
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.INSULIN_CORRECTION,
                    name = localization.getString(Res.string.correction),
                    sortIndex = 1,
                    aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                    range = MeasurementValueRange(
                        minimum = 0.0,
                        low = null,
                        target = null,
                        high = null,
                        maximum = 100.0,
                        isHighlighted = false,
                    ),
                    unitSuggestions = listOf(
                        MeasurementUnitSuggestion.Seed(
                            factor = MeasurementUnitSuggestion.FACTOR_DEFAULT,
                            unit = DatabaseKey.MeasurementUnit.INSULIN_UNITS,
                        ),
                    ),
                ),
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.INSULIN_BASAL,
                    name = localization.getString(Res.string.basal),
                    sortIndex = 2,
                    aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                    range = MeasurementValueRange(
                        minimum = 0.0,
                        low = null,
                        target = null,
                        high = null,
                        maximum = 100.0,
                        isHighlighted = false,
                    ),
                    unitSuggestions = listOf(
                        MeasurementUnitSuggestion.Seed(
                            factor = MeasurementUnitSuggestion.FACTOR_DEFAULT,
                            unit = DatabaseKey.MeasurementUnit.INSULIN_UNITS,
                        ),
                    ),
                ),
            ),
        )
    }
}