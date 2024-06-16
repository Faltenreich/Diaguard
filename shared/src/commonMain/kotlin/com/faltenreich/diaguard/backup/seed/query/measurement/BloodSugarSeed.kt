package com.faltenreich.diaguard.backup.seed.query.measurement

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.blood_sugar
import diaguard.shared.generated.resources.milligrams_per_deciliter
import diaguard.shared.generated.resources.milligrams_per_deciliter_abbreviation
import diaguard.shared.generated.resources.millimoles_per_liter
import diaguard.shared.generated.resources.millimoles_per_liter_abbreviation

class BloodSugarSeed(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.BLOOD_SUGAR,
            name = localization.getString(Res.string.blood_sugar),
            icon = "\uD83E\uDE78",
            sortIndex = 0,
            isActive = true,
            properties = listOf(
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                    name = localization.getString(Res.string.blood_sugar),
                    sortIndex = 0,
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
                        MeasurementUnit.Seed(
                            key = DatabaseKey.MeasurementUnit.BLOOD_SUGAR_MILLIGRAMS_PER_DECILITER,
                            name = localization.getString(Res.string.milligrams_per_deciliter),
                            abbreviation = localization.getString(Res.string.milligrams_per_deciliter_abbreviation),
                            factor = 1.0,
                            isSelected = true,
                        ),
                        MeasurementUnit.Seed(
                            key = DatabaseKey.MeasurementUnit.BLOOD_SUGAR_MILLIMOLES_PER_LITER,
                            name = localization.getString(Res.string.millimoles_per_liter),
                            abbreviation = localization.getString(Res.string.millimoles_per_liter_abbreviation),
                            factor = 0.0555,
                            isSelected = false,
                        ),
                    ),
                )
            ),
        )
    }
}