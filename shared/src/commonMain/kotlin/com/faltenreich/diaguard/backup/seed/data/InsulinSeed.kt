package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey

class InsulinSeed {

    operator fun invoke(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = DatabaseKey.MeasurementProperty.INSULIN,
            name = MR.strings.insulin,
            icon = "\uD83D\uDC89",
            types = listOf(
                SeedMeasurementType(
                    key = DatabaseKey.MeasurementType.INSULIN_BOLUS,
                    name = MR.strings.bolus,
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
                            name = MR.strings.insulin_units,
                            abbreviation = MR.strings.insulin_units_abbreviation,
                            factor = 1.0,
                        )
                    ),
                ),
                SeedMeasurementType(
                    key = DatabaseKey.MeasurementType.INSULIN_CORRECTION,
                    name = MR.strings.correction,
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
                            name = MR.strings.insulin_units,
                            abbreviation = MR.strings.insulin_units_abbreviation,
                            factor = 1.0,
                        )
                    ),
                ),
                SeedMeasurementType(
                    key = DatabaseKey.MeasurementType.INSULIN_BASAL,
                    name = MR.strings.basal,
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
                            name = MR.strings.insulin_units,
                            abbreviation = MR.strings.insulin_units_abbreviation,
                            factor = 1.0,
                        )
                    ),
                ),
            ),
        )
    }
}