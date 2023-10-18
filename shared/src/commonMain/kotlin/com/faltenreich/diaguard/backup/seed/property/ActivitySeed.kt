package com.faltenreich.diaguard.backup.seed.property

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.Seed
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit

class ActivitySeed : Seed<SeedMeasurementProperty> {

    override fun harvest(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = "activity",
            name = MR.strings.activity,
            icon = "\uD83C\uDFC3",
            type = SeedMeasurementType(
                key = "activity",
                name = MR.strings.activity,
                unit = SeedMeasurementUnit(
                    key = "minutes",
                    name = MR.strings.minutes,
                    abbreviation = MR.strings.minutes_abbreviation,
                    factor = 1.0,
                ),
            ),
        )
    }
}