package com.faltenreich.diaguard.backup.seed.query.measurement

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hba1c
import diaguard.shared.generated.resources.millimoles_per_mole
import diaguard.shared.generated.resources.millimoles_per_mole_abbreviation
import diaguard.shared.generated.resources.percent
import diaguard.shared.generated.resources.percent_abbreviation

class HbA1cSeedQueries(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.HBA1C,
            name = localization.getString(Res.string.hba1c),
            icon = "%",
            sortIndex = 4,
            isActive = true,
            properties = listOf(
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.HBA1C,
                    name = localization.getString(Res.string.hba1c),
                    sortIndex = 0,
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
                        MeasurementUnit.Seed(
                            key = DatabaseKey.MeasurementUnit.HBA1C_PERCENT,
                            name = localization.getString(Res.string.percent),
                            abbreviation = localization.getString(Res.string.percent_abbreviation),
                            factor = 1.0,
                            isSelected = true,
                        ),
                        MeasurementUnit.Seed(
                            key = DatabaseKey.MeasurementUnit.HBA1C_MILLIMOLES_PER_MOLES,
                            name = localization.getString(Res.string.millimoles_per_mole),
                            abbreviation = localization.getString(Res.string.millimoles_per_mole_abbreviation),
                            factor = 0.00001,
                            isSelected = false,
                        ),
                    ),
                ),
            ),
        )
    }
}