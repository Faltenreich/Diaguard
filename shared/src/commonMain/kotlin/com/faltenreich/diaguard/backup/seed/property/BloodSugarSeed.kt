package com.faltenreich.diaguard.backup.seed.property

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.Seed
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit

class BloodSugarSeed : Seed<SeedMeasurementProperty> {

    override fun harvest(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = KEY,
            name = MR.strings.blood_sugar,
            icon = "\uD83E\uDE78",
            type = SeedMeasurementType(
                key = "blood_sugar",
                name = MR.strings.blood_sugar,
                units = listOf(
                    SeedMeasurementUnit(
                        key = "milligrams_per_deciliter",
                        name = MR.strings.milligrams_per_deciliter,
                        abbreviation = MR.strings.milligrams_per_deciliter_abbreviation,
                        factor = 1.0,
                    ),
                    SeedMeasurementUnit(
                        key = "millimoles_per_liter",
                            name = MR.strings.millimoles_per_liter,
                            abbreviation = MR.strings.millimoles_per_liter_abbreviation,
                            factor = 0.0555,
                    ),
                ),
            ),
        )
    }

    companion object {

        const val KEY = "blood_sugar"
    }
}