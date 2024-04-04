package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey

class ActivitySeed {

    operator fun invoke(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = DatabaseKey.MeasurementProperty.ACTIVITY,
            name = MR.strings.activity,
            icon = "\uD83C\uDFC3",
            type = SeedMeasurementType(
                key = DatabaseKey.MeasurementType.ACTIVITY,
                name = MR.strings.activity,
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
                        name = MR.strings.minutes,
                        abbreviation = MR.strings.minutes_abbreviation,
                        factor = 1.0,
                    )
                ),
            ),
        )
    }
}