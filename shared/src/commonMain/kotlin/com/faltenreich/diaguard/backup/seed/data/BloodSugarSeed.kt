package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseKey

class BloodSugarSeed {

    operator fun invoke(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
            name = MR.strings.blood_sugar,
            icon = "\uD83E\uDE78",
            type = SeedMeasurementType(
                key = DatabaseKey.MeasurementType.BLOOD_SUGAR,
                name = MR.strings.blood_sugar,
                minimumValue = 1.0,
                maximumValue = 1000.0,
                units = listOf(
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.BLOOD_SUGAR_MILLIGRAMS_PER_DECILITER,
                        name = MR.strings.milligrams_per_deciliter,
                        abbreviation = MR.strings.milligrams_per_deciliter_abbreviation,
                        factor = 1.0,
                    ),
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.BLOOD_SUGAR_MILLIMOLES_PER_LITER,
                        name = MR.strings.millimoles_per_liter,
                        abbreviation = MR.strings.millimoles_per_liter_abbreviation,
                        factor = 0.0555,
                    ),
                ),
            ),
        )
    }
}