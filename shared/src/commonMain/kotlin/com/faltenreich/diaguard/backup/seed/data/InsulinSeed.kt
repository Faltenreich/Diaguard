package com.faltenreich.diaguard.backup.seed.data

import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.backup.seed.SeedMeasurementCategory
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey

class InsulinSeed {

    operator fun invoke(): SeedMeasurementCategory {
        return SeedMeasurementCategory(
            key = DatabaseKey.MeasurementCategory.INSULIN,
            name = Res.string.insulin,
            icon = "\uD83D\uDC89",
            types = listOf(
                SeedMeasurementType(
                    key = DatabaseKey.MeasurementType.INSULIN_BOLUS,
                    name = Res.string.bolus,
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
                SeedMeasurementType(
                    key = DatabaseKey.MeasurementType.INSULIN_CORRECTION,
                    name = Res.string.correction,
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
                SeedMeasurementType(
                    key = DatabaseKey.MeasurementType.INSULIN_BASAL,
                    name = Res.string.basal,
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