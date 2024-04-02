package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.value.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey

class HbA1cSeed {

    operator fun invoke(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = DatabaseKey.MeasurementProperty.HBA1C,
            name = MR.strings.hba1c,
            icon = "%",
            type = SeedMeasurementType(
                key = DatabaseKey.MeasurementType.HBA1C,
                name = MR.strings.hba1c,
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
                        name = MR.strings.percent,
                        abbreviation = MR.strings.percent_abbreviation,
                        factor = 1.0,
                    ),
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.HBA1C_MILLIMOLES_PER_MOLES,
                        name = MR.strings.millimoles_per_mole,
                        abbreviation = MR.strings.millimoles_per_mole_abbreviation,
                        factor = 0.00001,
                    ),
                ),
            ),
        )
    }
}