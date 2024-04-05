package com.faltenreich.diaguard.backup.seed.data

import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey

class HbA1cSeed {

    operator fun invoke(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = DatabaseKey.MeasurementProperty.HBA1C,
            name = Res.string.hba1c,
            icon = "%",
            type = SeedMeasurementType(
                key = DatabaseKey.MeasurementType.HBA1C,
                name = Res.string.hba1c,
                range = MeasurementValueRange(
                    minimum = 1.0,
                    low = 6.5,
                    target = 7.0,
                    high = 7.5,
                    maximum = 25.0,
                    isHighlighted = true,
                ),
                units = listOf(
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.HBA1C_PERCENT,
                        name = Res.string.percent,
                        abbreviation = Res.string.percent_abbreviation,
                        factor = 1.0,
                    ),
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.HBA1C_MILLIMOLES_PER_MOLES,
                        name = Res.string.millimoles_per_mole,
                        abbreviation = Res.string.millimoles_per_mole_abbreviation,
                        factor = 0.00001,
                    ),
                ),
            ),
        )
    }
}