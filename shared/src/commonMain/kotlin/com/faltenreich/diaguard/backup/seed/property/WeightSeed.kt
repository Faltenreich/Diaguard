package com.faltenreich.diaguard.backup.seed.property

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.Seed
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit

class WeightSeed : Seed<SeedMeasurementProperty> {

    override fun harvest(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = "weight",
            name = MR.strings.weight,
            icon = "\uD83C\uDFCB",
            type = SeedMeasurementType(
                key = "weight",
                name = MR.strings.weight,
                units = listOf(
                    SeedMeasurementUnit(
                        key = "kilograms",
                        name = MR.strings.kilograms,
                        abbreviation = MR.strings.kilograms_abbreviation,
                        factor = 1.0,
                    ),
                    SeedMeasurementUnit(
                        key = "pounds",
                        name = MR.strings.pounds,
                        abbreviation = MR.strings.pounds_abbreviation,
                        factor = 2.20462262185,
                    ),
                ),
            ),
        )
    }
}