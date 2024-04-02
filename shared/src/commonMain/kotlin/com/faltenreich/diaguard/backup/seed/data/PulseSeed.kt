package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.value.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey

class PulseSeed {

    operator fun invoke(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = DatabaseKey.MeasurementProperty.PULSE,
            name = MR.strings.pulse,
            icon = "\uD83D\uDC9A",
            type = SeedMeasurementType(
                key = DatabaseKey.MeasurementType.PULSE,
                name = MR.strings.pulse,
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
                        name = MR.strings.beats_per_minute,
                        abbreviation = MR.strings.beats_per_minute_abbreviation,
                        factor = 1.0,
                    )
                ),
            ),
        )
    }
}