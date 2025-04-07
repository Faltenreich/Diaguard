package com.faltenreich.diaguard.backup.seed.query.measurement

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.weight

class WeightSeedQueries(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.WEIGHT,
            name = localization.getString(Res.string.weight),
            icon = "\uD83C\uDFCB",
            sortIndex = 5,
            isActive = true,
            properties = listOf(
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.WEIGHT,
                    name = localization.getString(Res.string.weight),
                    sortIndex = 0,
                    aggregationStyle = MeasurementAggregationStyle.AVERAGE,
                    range = MeasurementValueRange(
                        minimum = 1.0,
                        low = null,
                        target = null,
                        high = null,
                        maximum = 1400.0,
                        isHighlighted = false,
                    ),
                    units = listOf(
                        DatabaseKey.MeasurementUnit.KILOGRAMS,
                        DatabaseKey.MeasurementUnit.POUNDS,
                    ),
                ),
            ),
        )
    }
}