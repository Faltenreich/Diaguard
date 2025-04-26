package com.faltenreich.diaguard.startup.seed.query.measurement

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.aggregationstyle.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.blood_pressure
import diaguard.shared.generated.resources.diastolic
import diaguard.shared.generated.resources.systolic

class BloodPressureSeedQueries(
    private val localization: Localization,
) {

    operator fun invoke(): MeasurementCategory.Seed {
        return MeasurementCategory.Seed(
            key = DatabaseKey.MeasurementCategory.BLOOD_PRESSURE,
            name = localization.getString(Res.string.blood_pressure),
            icon = "⛽",
            sortIndex = 7,
            isActive = true,
            properties = listOf(
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_SYSTOLIC,
                    name = localization.getString(Res.string.systolic),
                    sortIndex = 0,
                    aggregationStyle = MeasurementAggregationStyle.AVERAGE,
                    range = MeasurementValueRange(
                        minimum = 1.0,
                        low = 100.0,
                        target = 120.0,
                        high = 140.0,
                        maximum = 300.0,
                        isHighlighted = true,
                    ),
                    unitSuggestions = listOf(
                        MeasurementUnitSuggestion.Seed(
                            factor = MeasurementUnitSuggestion.FACTOR_DEFAULT,
                            unit = DatabaseKey.MeasurementUnit.MILLIMETERS_OF_MERCURY,
                        ),
                    ),
                ),
                MeasurementProperty.Seed(
                    key = DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_DIASTOLIC,
                    name = localization.getString(Res.string.diastolic),
                    sortIndex = 1,
                    aggregationStyle = MeasurementAggregationStyle.AVERAGE,
                    range = MeasurementValueRange(
                        minimum = 1.0,
                        low = 60.0,
                        target = 80.0,
                        high = 90.0,
                        maximum = 300.0,
                        isHighlighted = true,
                    ),
                    unitSuggestions = listOf(
                        MeasurementUnitSuggestion.Seed(
                            factor = MeasurementUnitSuggestion.FACTOR_DEFAULT,
                            unit = DatabaseKey.MeasurementUnit.MILLIMETERS_OF_MERCURY,
                        ),
                    ),
                ),
            ),
        )
    }
}