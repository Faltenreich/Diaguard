package com.faltenreich.diaguard.backup.seed.property

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.Seed
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit

class OxygenSaturationSeed : Seed<SeedMeasurementProperty> {

    override fun harvest(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = "oxygen_saturation",
            name = MR.strings.oxygen_saturation,
            icon = "O²",
            type = SeedMeasurementType(
                key = "oxygen_saturation",
                name = MR.strings.oxygen_saturation,
                unit = SeedMeasurementUnit(
                    key = "percent",
                    name = MR.strings.percent,
                    abbreviation = MR.strings.percent_abbreviation,
                    factor = 1.0,
                ),
            ),
        )
    }
}