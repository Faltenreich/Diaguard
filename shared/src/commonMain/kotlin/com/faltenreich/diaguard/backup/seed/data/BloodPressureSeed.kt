package com.faltenreich.diaguard.backup.seed.data

import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.backup.seed.SeedMeasurementCategory
import com.faltenreich.diaguard.backup.seed.SeedMeasurementType
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey

class BloodPressureSeed {

    operator fun invoke(): SeedMeasurementCategory {
        return SeedMeasurementCategory(
            key = DatabaseKey.MeasurementCategory.BLOOD_PRESSURE,
            name = Res.string.blood_pressure,
            icon = "⛽",
            types = listOf(
                SeedMeasurementType(
                    key = DatabaseKey.MeasurementType.BLOOD_PRESSURE_SYSTOLIC,
                    name = Res.string.systolic,
                    range = MeasurementValueRange(
                        minimum = 1.0,
                        low = 100.0,
                        target = 120.0,
                        high = 140.0,
                        maximum = 300.0,
                        isHighlighted = true,
                    ),
                    units = listOf(
                        SeedMeasurementUnit(
                            key = DatabaseKey.MeasurementUnit.BLOOD_PRESSURE_SYSTOLIC,
                            name = Res.string.millimeters_of_mercury,
                            abbreviation = Res.string.millimeters_of_mercury_abbreviation,
                            factor = 1.0,
                        )
                    ),
                ),
                SeedMeasurementType(
                    key = DatabaseKey.MeasurementType.BLOOD_PRESSURE_DIASTOLIC,
                    name = Res.string.diastolic,
                    range = MeasurementValueRange(
                        minimum = 1.0,
                        low = 60.0,
                        target = 80.0,
                        high = 90.0,
                        maximum = 300.0,
                        isHighlighted = true,
                    ),
                    units = listOf(
                        SeedMeasurementUnit(
                            key = DatabaseKey.MeasurementUnit.BLOOD_PRESSURE_DIASTOLIC,
                            name = Res.string.millimeters_of_mercury,
                            abbreviation = Res.string.millimeters_of_mercury_abbreviation,
                            factor = 1.0,
                        )
                    ),
                ),
            ),
        )
    }
}