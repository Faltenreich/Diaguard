package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseKey

class WeightSeed {

    operator fun invoke(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = DatabaseKey.MeasurementProperty.WEIGHT,
            name = MR.strings.weight,
            icon = "\uD83C\uDFCB",
            type = SeedMeasurementType(
                key = DatabaseKey.MeasurementType.WEIGHT,
                name = MR.strings.weight,
                minimumValue = 1.0,
                lowValue = null,
                targetValue = null,
                highValue = null,
                maximumValue = 1400.0,
                units = listOf(
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.WEIGHT_KILOGRAMS,
                        name = MR.strings.kilograms,
                        abbreviation = MR.strings.kilograms_abbreviation,
                        factor = 1.0,
                    ),
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.WEIGHT_POUNDS,
                        name = MR.strings.pounds,
                        abbreviation = MR.strings.pounds_abbreviation,
                        factor = 2.20462262185,
                    ),
                ),
            ),
        )
    }
}