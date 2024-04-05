package com.faltenreich.diaguard.backup.seed.data

import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey

class OxygenSaturationSeed {

    operator fun invoke(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = DatabaseKey.MeasurementProperty.OXYGEN_SATURATION,
            name = Res.string.oxygen_saturation,
            icon = "O²",
            type = SeedMeasurementType(
                key = DatabaseKey.MeasurementType.OXYGEN_SATURATION,
                name = Res.string.oxygen_saturation,
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
                        name = Res.string.percent,
                        abbreviation = Res.string.percent_abbreviation,
                        factor = 1.0,
                    )
                ),
            ),
        )
    }
}