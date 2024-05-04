package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.backup.seed.SeedMeasurementCategory
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.blood_pressure
import diaguard.shared.generated.resources.diastolic
import diaguard.shared.generated.resources.millimeters_of_mercury
import diaguard.shared.generated.resources.millimeters_of_mercury_abbreviation
import diaguard.shared.generated.resources.systolic

class BloodPressureSeed {

    operator fun invoke(): SeedMeasurementCategory {
        return SeedMeasurementCategory(
            key = DatabaseKey.MeasurementCategory.BLOOD_PRESSURE,
            name = Res.string.blood_pressure,
            icon = "⛽",
            properties = listOf(
                SeedMeasurementProperty(
                    key = DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_SYSTOLIC,
                    name = Res.string.systolic,
                    aggregationStyle = MeasurementAggregationStyle.AVERAGE,
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
                SeedMeasurementProperty(
                    key = DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_DIASTOLIC,
                    name = Res.string.diastolic,
                    aggregationStyle = MeasurementAggregationStyle.AVERAGE,
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