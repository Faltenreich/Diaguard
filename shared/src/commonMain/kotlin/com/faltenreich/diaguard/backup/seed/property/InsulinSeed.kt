package com.faltenreich.diaguard.backup.seed.property

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.Seed
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseKey

class InsulinSeed : Seed<SeedMeasurementProperty> {

    override fun harvest(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = DatabaseKey.MeasurementProperty.INSULIN,
            name = MR.strings.insulin,
            icon = "\uD83D\uDC89",
            types = listOf(
                SeedMeasurementType(
                    key = DatabaseKey.MeasurementType.INSULIN_BOLUS,
                    name = MR.strings.bolus,
                    unit = SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.INSULIN_BOLUS,
                        name = MR.strings.insulin_units,
                        abbreviation = MR.strings.insulin_units_abbreviation,
                        factor = 1.0,
                    ),
                ),
                SeedMeasurementType(
                    key = DatabaseKey.MeasurementType.INSULIN_CORRECTION,
                    name = MR.strings.correction,
                    unit = SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.INSULIN_CORRECTION,
                        name = MR.strings.insulin_units,
                        abbreviation = MR.strings.insulin_units_abbreviation,
                        factor = 1.0,
                    ),
                ),
                SeedMeasurementType(
                    key = DatabaseKey.MeasurementType.INSULIN_BASAL,
                    name = MR.strings.basal,
                    unit = SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.INSULIN_BASAL,
                        name = MR.strings.insulin_units,
                        abbreviation = MR.strings.insulin_units_abbreviation,
                        factor = 1.0,
                    ),
                ),
            ),
        )
    }
}