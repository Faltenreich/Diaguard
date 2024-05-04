package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.backup.seed.SeedMeasurementCategory
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.basal
import diaguard.shared.generated.resources.bolus
import diaguard.shared.generated.resources.correction
import diaguard.shared.generated.resources.insulin
import diaguard.shared.generated.resources.insulin_units
import diaguard.shared.generated.resources.insulin_units_abbreviation

class InsulinSeed {

    operator fun invoke(): SeedMeasurementCategory {
        return SeedMeasurementCategory(
            key = DatabaseKey.MeasurementCategory.INSULIN,
            name = Res.string.insulin,
            icon = "\uD83D\uDC89",
            properties = listOf(
                SeedMeasurementProperty(
                    key = DatabaseKey.MeasurementProperty.INSULIN_BOLUS,
                    name = Res.string.bolus,
                    aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                    range = MeasurementValueRange(
                        minimum = 0.0,
                        low = null,
                        target = null,
                        high = null,
                        maximum = 100.0,
                        isHighlighted = false,
                    ),
                    units = listOf(
                        SeedMeasurementUnit(
                            key = DatabaseKey.MeasurementUnit.INSULIN_BOLUS,
                            name = Res.string.insulin_units,
                            abbreviation = Res.string.insulin_units_abbreviation,
                            factor = 1.0,
                        )
                    ),
                ),
                SeedMeasurementProperty(
                    key = DatabaseKey.MeasurementProperty.INSULIN_CORRECTION,
                    name = Res.string.correction,
                    aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                    range = MeasurementValueRange(
                        minimum = 0.0,
                        low = null,
                        target = null,
                        high = null,
                        maximum = 100.0,
                        isHighlighted = false,
                    ),
                    units = listOf(
                        SeedMeasurementUnit(
                            key = DatabaseKey.MeasurementUnit.INSULIN_CORRECTION,
                            name = Res.string.insulin_units,
                            abbreviation = Res.string.insulin_units_abbreviation,
                            factor = 1.0,
                        )
                    ),
                ),
                SeedMeasurementProperty(
                    key = DatabaseKey.MeasurementProperty.INSULIN_BASAL,
                    name = Res.string.basal,
                    aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                    range = MeasurementValueRange(
                        minimum = 0.0,
                        low = null,
                        target = null,
                        high = null,
                        maximum = 100.0,
                        isHighlighted = false,
                    ),
                    units = listOf(
                        SeedMeasurementUnit(
                            key = DatabaseKey.MeasurementUnit.INSULIN_BASAL,
                            name = Res.string.insulin_units,
                            abbreviation = Res.string.insulin_units_abbreviation,
                            factor = 1.0,
                        )
                    ),
                ),
            ),
        )
    }
}