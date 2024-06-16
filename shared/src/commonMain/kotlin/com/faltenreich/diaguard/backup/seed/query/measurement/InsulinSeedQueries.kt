package com.faltenreich.diaguard.backup.seed.query.measurement

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.basal
import diaguard.shared.generated.resources.bolus
import diaguard.shared.generated.resources.correction
import diaguard.shared.generated.resources.insulin
import diaguard.shared.generated.resources.insulin_units
import diaguard.shared.generated.resources.insulin_units_abbreviation

class InsulinSeedQueries(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.INSULIN,
            name = localization.getString(Res.string.insulin),
            icon = "\uD83D\uDC89",
            sortIndex = 1,
            isActive = true,
            properties = listOf(
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.INSULIN_BOLUS,
                    name = localization.getString(Res.string.bolus),
                    sortIndex = 0,
                    aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                    range = MeasurementValueRange(
                        minimum = 0.0,
                        low = null,
                        target = null,
                        high = null,
                        maximum = 100.0,
                        isHighlighted = false,
                    ),
                    units = listOf(
                        MeasurementUnit.Seed(
                            key = DatabaseKey.MeasurementUnit.INSULIN_BOLUS,
                            name = localization.getString(Res.string.insulin_units),
                            abbreviation = localization.getString(Res.string.insulin_units_abbreviation),
                            factor = 1.0,
                            isSelected = true,
                        )
                    ),
                ),
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.INSULIN_CORRECTION,
                    name = localization.getString(Res.string.correction),
                    sortIndex = 1,
                    aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                    range = MeasurementValueRange(
                        minimum = 0.0,
                        low = null,
                        target = null,
                        high = null,
                        maximum = 100.0,
                        isHighlighted = false,
                    ),
                    units = listOf(
                        MeasurementUnit.Seed(
                            key = DatabaseKey.MeasurementUnit.INSULIN_CORRECTION,
                            name = localization.getString(Res.string.insulin_units),
                            abbreviation = localization.getString(Res.string.insulin_units_abbreviation),
                            factor = 1.0,
                            isSelected = true,
                        )
                    ),
                ),
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.INSULIN_BASAL,
                    name = localization.getString(Res.string.basal),
                    sortIndex = 2,
                    aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                    range = MeasurementValueRange(
                        minimum = 0.0,
                        low = null,
                        target = null,
                        high = null,
                        maximum = 100.0,
                        isHighlighted = false,
                    ),
                    units = listOf(
                        MeasurementUnit.Seed(
                            key = DatabaseKey.MeasurementUnit.INSULIN_BASAL,
                            name = localization.getString(Res.string.insulin_units),
                            abbreviation = localization.getString(Res.string.insulin_units_abbreviation),
                            factor = 1.0,
                            isSelected = true,
                        )
                    ),
                ),
            ),
        )
    }
}