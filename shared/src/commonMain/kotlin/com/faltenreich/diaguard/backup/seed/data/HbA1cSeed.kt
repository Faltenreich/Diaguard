package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.backup.seed.SeedMeasurementCategory
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hba1c
import diaguard.shared.generated.resources.millimoles_per_mole
import diaguard.shared.generated.resources.millimoles_per_mole_abbreviation
import diaguard.shared.generated.resources.percent
import diaguard.shared.generated.resources.percent_abbreviation

class HbA1cSeed {

    operator fun invoke(): SeedMeasurementCategory {
        return SeedMeasurementCategory(
            key = DatabaseKey.MeasurementCategory.HBA1C,
            name = Res.string.hba1c,
            icon = "%",
            property = SeedMeasurementProperty(
                key = DatabaseKey.MeasurementProperty.HBA1C,
                name = Res.string.hba1c,
                aggregationStyle = MeasurementAggregationStyle.AVERAGE,
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