package com.faltenreich.diaguard.backup.seed.data

import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey

class WeightSeed {

    operator fun invoke(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = DatabaseKey.MeasurementProperty.WEIGHT,
            name = Res.string.weight,
            icon = "\uD83C\uDFCB",
            type = SeedMeasurementType(
                key = DatabaseKey.MeasurementType.WEIGHT,
                name = Res.string.weight,
                range = MeasurementValueRange(
                    minimum = 1.0,
                    low = null,
                    target = null,
                    high = null,
                    maximum = 1400.0,
                    isHighlighted = false,
                ),
                units = listOf(
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.WEIGHT_KILOGRAMS,
                        name = Res.string.kilograms,
                        abbreviation = Res.string.kilograms_abbreviation,
                        factor = 1.0,
                    ),
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.WEIGHT_POUNDS,
                        name = Res.string.pounds,
                        abbreviation = Res.string.pounds_abbreviation,
                        factor = 2.20462262185,
                    ),
                ),
            ),
        )
    }
}