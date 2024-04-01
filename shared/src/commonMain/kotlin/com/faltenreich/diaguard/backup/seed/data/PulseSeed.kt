package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
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
                minimumValue = 1.0,
                lowValue = 60.0,
                highValue = 80.0,
                maximumValue = 200.0,
                unit = SeedMeasurementUnit(
                    key = DatabaseKey.MeasurementUnit.PULSE,
                    name = MR.strings.beats_per_minute,
                    abbreviation = MR.strings.beats_per_minute_abbreviation,
                    factor = 1.0,
                ),
            ),
        )
    }
}