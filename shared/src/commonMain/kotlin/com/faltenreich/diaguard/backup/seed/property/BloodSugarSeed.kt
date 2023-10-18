package com.faltenreich.diaguard.backup.seed.property

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.Seed
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

class BloodSugarSeed : Seed<SeedMeasurementProperty> {

    override fun harvest(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = MeasurementProperty.Key.BLOOD_SUGAR,
            name = MR.strings.blood_sugar,
            icon = "\uD83E\uDE78",
            type = SeedMeasurementType(
                key = MeasurementType.Key.BLOOD_SUGAR,
                name = MR.strings.blood_sugar,
                units = listOf(
                    SeedMeasurementUnit(
                        key = MeasurementUnit.Key.BLOOD_SUGAR_MG_DL,
                        name = MR.strings.milligrams_per_deciliter,
                        abbreviation = MR.strings.milligrams_per_deciliter_abbreviation,
                        factor = 1.0,
                    ),
                    SeedMeasurementUnit(
                        key = MeasurementUnit.Key.BLOOD_SUGAR_MMOL_L,
                            name = MR.strings.millimoles_per_liter,
                            abbreviation = MR.strings.millimoles_per_liter_abbreviation,
                            factor = 0.0555,
                    ),
                ),
            ),
        )
    }
}