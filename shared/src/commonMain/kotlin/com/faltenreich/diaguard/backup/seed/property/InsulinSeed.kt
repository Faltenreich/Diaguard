package com.faltenreich.diaguard.backup.seed.property

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.Seed
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit

class InsulinSeed : Seed<SeedMeasurementProperty> {

    override fun harvest(): SeedMeasurementProperty {
        val unit = SeedMeasurementUnit(
            key = "insulin_units",
            name = MR.strings.insulin_units,
            abbreviation = MR.strings.insulin_units_abbreviation,
            factor = 1.0,
        )
        return SeedMeasurementProperty(
            key = "insulin",
            name = MR.strings.insulin,
            icon = "\uD83D\uDC89",
            types = listOf(
                SeedMeasurementType(
                    key = "bolus",
                    name = MR.strings.bolus,
                    unit = unit,
                ),
                SeedMeasurementType(
                    key = "correction",
                    name = MR.strings.correction,
                    unit = unit,
                ),
                SeedMeasurementType(
                    key = "basal",
                    name = MR.strings.basal,
                    unit = unit,
                ),
            ),
        )
    }
}