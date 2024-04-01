package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseKey

class BloodPressureSeed {

    operator fun invoke(): SeedMeasurementProperty {
        return SeedMeasurementProperty(
            key = DatabaseKey.MeasurementProperty.BLOOD_PRESSURE,
            name = MR.strings.blood_pressure,
            icon = "⛽",
            types = listOf(
                SeedMeasurementType(
                    key = DatabaseKey.MeasurementType.BLOOD_PRESSURE_SYSTOLIC,
                    name = MR.strings.systolic,
                    minimumValue = 1.0,
                    lowValue = 1.0,
                    highValue = 129.0,
                    maximumValue = 300.0,
                    unit = SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.BLOOD_PRESSURE_SYSTOLIC,
                        name = MR.strings.millimeters_of_mercury,
                        abbreviation = MR.strings.millimeters_of_mercury_abbreviation,
                        factor = 1.0,
                    ),
                ),
                SeedMeasurementType(
                    key = DatabaseKey.MeasurementType.BLOOD_PRESSURE_DIASTOLIC,
                    name = MR.strings.diastolic,
                    minimumValue = 1.0,
                    lowValue = 1.0,
                    highValue = 84.0,
                    maximumValue = 300.0,
                    unit = SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.BLOOD_PRESSURE_DIASTOLIC,
                        name = MR.strings.millimeters_of_mercury,
                        abbreviation = MR.strings.millimeters_of_mercury_abbreviation,
                        factor = 1.0,
                    ),
                ),
            ),
        )
    }
}