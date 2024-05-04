package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.backup.seed.SeedMeasurementCategory
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.kilograms
import diaguard.shared.generated.resources.kilograms_abbreviation
import diaguard.shared.generated.resources.pounds
import diaguard.shared.generated.resources.pounds_abbreviation
import diaguard.shared.generated.resources.weight

class WeightSeed {

    operator fun invoke(): SeedMeasurementCategory {
        return SeedMeasurementCategory(
            key = DatabaseKey.MeasurementCategory.WEIGHT,
            name = Res.string.weight,
            icon = "\uD83C\uDFCB",
            property = SeedMeasurementProperty(
                key = DatabaseKey.MeasurementProperty.WEIGHT,
                name = Res.string.weight,
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
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.WEIGHT_KILOGRAMS,
                        name = Res.string.kilograms,
                        abbreviation = Res.string.kilograms_abbreviation,
                        factor = 1.0,
                    ),
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.WEIGHT_POUNDS,
                        name = Res.string.pounds,
                        abbreviation = Res.string.pounds_abbreviation,
                        factor = 2.20462262185,
                    ),
                ),
            ),
        )
    }
}