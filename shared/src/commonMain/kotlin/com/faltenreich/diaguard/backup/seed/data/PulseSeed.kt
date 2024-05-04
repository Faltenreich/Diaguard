package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.backup.seed.SeedMeasurementCategory
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.beats_per_minute
import diaguard.shared.generated.resources.beats_per_minute_abbreviation
import diaguard.shared.generated.resources.pulse

class PulseSeed {

    operator fun invoke(): SeedMeasurementCategory {
        return SeedMeasurementCategory(
            key = DatabaseKey.MeasurementCategory.PULSE,
            name = Res.string.pulse,
            icon = "\uD83D\uDC9A",
            property = SeedMeasurementProperty(
                key = DatabaseKey.MeasurementProperty.PULSE,
                name = Res.string.pulse,
                aggregationStyle = MeasurementAggregationStyle.AVERAGE,
                range = MeasurementValueRange(
                    minimum = 1.0,
                    low = 60.0,
                    target = 70.0,
                    high = 80.0,
                    maximum = 200.0,
                    isHighlighted = true,
                ),
                units = listOf(
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.PULSE,
                        name = Res.string.beats_per_minute,
                        abbreviation = Res.string.beats_per_minute_abbreviation,
                        factor = 1.0,
                    )
                ),
            ),
        )
    }
}