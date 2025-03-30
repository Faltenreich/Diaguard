package com.faltenreich.diaguard.backup.seed.query.measurement

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.activity
import diaguard.shared.generated.resources.minutes
import diaguard.shared.generated.resources.minutes_abbreviation

class ActivitySeedQueries(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.ACTIVITY,
            name = localization.getString(Res.string.activity),
            icon = "\uD83C\uDFC3",
            sortIndex = 3,
            isActive = true,
            properties = listOf(
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.ACTIVITY,
                    name = localization.getString(Res.string.activity),
                    sortIndex = 0,
                    aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                    range = MeasurementValueRange(
                        minimum = 1.0,
                        low = null,
                        target = null,
                        high = null,
                        maximum = 1000.0,
                        isHighlighted = false,
                    ),
                    units = listOf(
                        MeasurementUnit.Seed(
                            key = DatabaseKey.MeasurementUnit.ACTIVITY,
                            name = localization.getString(Res.string.minutes),
                            abbreviation = localization.getString(Res.string.minutes_abbreviation),
                            factor = 1.0,
                            isSelected = true,
                        )
                    ),
                ),
            ),
        )
    }
}