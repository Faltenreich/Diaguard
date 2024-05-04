package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.backup.seed.SeedMeasurementCategory
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.activity
import diaguard.shared.generated.resources.minutes
import diaguard.shared.generated.resources.minutes_abbreviation

class ActivitySeed {

    operator fun invoke(): SeedMeasurementCategory {
        return SeedMeasurementCategory(
            key = DatabaseKey.MeasurementCategory.ACTIVITY,
            name = Res.string.activity,
            icon = "\uD83C\uDFC3",
            property = SeedMeasurementProperty(
                key = DatabaseKey.MeasurementProperty.ACTIVITY,
                name = Res.string.activity,
                aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                range = MeasurementValueRange(
                    minimum = 1.0,
                    low = null,
                    target = null,
                    high = null,
                    maximum = 1000.0,
                    isHighlighted = false,
                ),
                units = listOf(
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.ACTIVITY,
                        name = Res.string.minutes,
                        abbreviation = Res.string.minutes_abbreviation,
                        factor = 1.0,
                    )
                ),
            ),
        )
    }
}