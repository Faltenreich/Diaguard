package com.faltenreich.diaguard.backup.seed.query.measurement

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.kilograms
import diaguard.shared.generated.resources.kilograms_abbreviation
import diaguard.shared.generated.resources.pounds
import diaguard.shared.generated.resources.pounds_abbreviation
import diaguard.shared.generated.resources.weight

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
                    units = listOf(
                        MeasurementUnit.Seed(
                            key = DatabaseKey.MeasurementUnit.WEIGHT_KILOGRAMS,
                            name = localization.getString(Res.string.kilograms),
                            abbreviation = localization.getString(Res.string.kilograms_abbreviation),
                            factor = 1.0,
                            isSelected = true, // TODO: Localize
                        ),
                        MeasurementUnit.Seed(
                            key = DatabaseKey.MeasurementUnit.WEIGHT_POUNDS,
                            name = localization.getString(Res.string.pounds),
                            abbreviation = localization.getString(Res.string.pounds_abbreviation),
                            factor = 2.20462262185,
                            isSelected = false,
                        ),
                    ),
                ),
            ),
        )
    }
}