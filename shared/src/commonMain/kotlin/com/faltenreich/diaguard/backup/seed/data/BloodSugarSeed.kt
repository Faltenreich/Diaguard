package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.backup.seed.SeedMeasurementCategory
import com.faltenreich.diaguard.backup.seed.SeedMeasurementProperty
import com.faltenreich.diaguard.backup.seed.SeedMeasurementUnit
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.blood_sugar
import diaguard.shared.generated.resources.milligrams_per_deciliter
import diaguard.shared.generated.resources.milligrams_per_deciliter_abbreviation
import diaguard.shared.generated.resources.millimoles_per_liter
import diaguard.shared.generated.resources.millimoles_per_liter_abbreviation

class BloodSugarSeed {

    operator fun invoke(): SeedMeasurementCategory {
        return SeedMeasurementCategory(
            key = DatabaseKey.MeasurementCategory.BLOOD_SUGAR,
            name = Res.string.blood_sugar,
            icon = "\uD83E\uDE78",
            property = SeedMeasurementProperty(
                key = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                name = Res.string.blood_sugar,
                aggregationStyle = MeasurementAggregationStyle.AVERAGE,
                range = MeasurementValueRange(
                    minimum = 1.0,
                    low = 60.0,
                    target = 120.0,
                    high = 180.0,
                    maximum = 1000.0,
                    isHighlighted = true,
                ),
                units = listOf(
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.BLOOD_SUGAR_MILLIGRAMS_PER_DECILITER,
                        name = Res.string.milligrams_per_deciliter,
                        abbreviation = Res.string.milligrams_per_deciliter_abbreviation,
                        factor = 1.0,
                    ),
                    SeedMeasurementUnit(
                        key = DatabaseKey.MeasurementUnit.BLOOD_SUGAR_MILLIMOLES_PER_LITER,
                        name = Res.string.millimoles_per_liter,
                        abbreviation = Res.string.millimoles_per_liter_abbreviation,
                        factor = 0.0555,
                    ),
                ),
            ),
        )
    }
}