package com.faltenreich.diaguard.backup.seed.property

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.Seed
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit

class PulseSeed : Seed<SeedMeasurementProperty> {

    override fun harvest(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = "pulse",
            name = MR.strings.pulse,
            icon = "\uD83D\uDC9A",
            type = SeedMeasurementType(
                key = "pulse",
                name = MR.strings.pulse,
                unit = SeedMeasurementUnit(
                    key = "beats_per_minute",
                    name = MR.strings.beats_per_minute,
                    abbreviation = MR.strings.beats_per_minute_abbreviation,
                    factor = 1.0,
                ),
            ),
        )
    }
}