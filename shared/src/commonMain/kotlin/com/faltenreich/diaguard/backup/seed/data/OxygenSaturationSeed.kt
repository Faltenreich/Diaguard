package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.value.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey

class OxygenSaturationSeed {

    operator fun invoke(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = DatabaseKey.MeasurementProperty.OXYGEN_SATURATION,
            name = MR.strings.oxygen_saturation,
            icon = "O²",
            type = SeedMeasurementType(
                key = DatabaseKey.MeasurementType.OXYGEN_SATURATION,
                name = MR.strings.oxygen_saturation,
                range = MeasurementValueRange(
                    minimum = 50.0,
                    low = 90.0,
                    target = 97.0,
                    high = 100.0,
                    maximum = 100.0,
                    isHighlighted = true,
                ),
                units = listOf(
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.OXYGEN_SATURATION,
                        name = MR.strings.percent,
                        abbreviation = MR.strings.percent_abbreviation,
                        factor = 1.0,
                    )
                ),
            ),
        )
    }
}