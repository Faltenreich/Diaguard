package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.beats_per_minute
import diaguard.shared.generated.resources.beats_per_minute_abbreviation
import diaguard.shared.generated.resources.pulse

class PulseSeed(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.PULSE,
            name = localization.getString(Res.string.pulse),
            icon = "\uD83D\uDC9A",
            sortIndex = 6,
            isActive = true,
            properties = listOf(
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.PULSE,
                    name = localization.getString(Res.string.pulse),
                    sortIndex = 0,
                    aggregationStyle = MeasurementAggregationStyle.AVERAGE,
                    range = MeasurementValueRange(
                        minimum = 1.0,
                        low = 60.0,
                        target = 70.0,
                        high = 80.0,
                        maximum = 200.0,
                        isHighlighted = true,
                    ),
                    units = listOf(
                        MeasurementUnit.Seed(
                            key = DatabaseKey.MeasurementUnit.PULSE,
                            name = localization.getString(Res.string.beats_per_minute),
                            abbreviation = localization.getString(Res.string.beats_per_minute_abbreviation),
                            factor = 1.0,
                            isSelected = true,
                        )
                    ),
                ),
            ),
        )
    }
}