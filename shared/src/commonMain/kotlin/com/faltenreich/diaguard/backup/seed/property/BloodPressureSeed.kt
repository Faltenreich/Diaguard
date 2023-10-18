package com.faltenreich.diaguard.backup.seed.property

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.Seed
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit

class BloodPressureSeed : Seed<SeedMeasurementProperty> {

    override fun harvest(): SeedMeasurementProperty {
        val unit = SeedMeasurementUnit(
            key = "millimeters_of_mercury",
            name = MR.strings.millimeters_of_mercury,
            abbreviation = MR.strings.millimeters_of_mercury_abbreviation,
            factor = 1.0,
        )
        return SeedMeasurementProperty(
            key = "blood_pressure",
            name = MR.strings.blood_pressure,
            icon = "⛽",
            types = listOf(
                SeedMeasurementType(
                    key = "systolic",
                    name = MR.strings.systolic,
                    unit = unit,
                ),
                SeedMeasurementType(
                    key = "diastolic",
                    name = MR.strings.diastolic,
                    unit = unit,
                ),
            ),
        )
    }
}