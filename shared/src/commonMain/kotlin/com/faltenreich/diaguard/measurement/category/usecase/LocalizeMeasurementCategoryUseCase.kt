package com.faltenreich.diaguard.measurement.category.usecase

import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.activity
import diaguard.shared.generated.resources.blood_pressure
import diaguard.shared.generated.resources.blood_sugar
import diaguard.shared.generated.resources.hba1c
import diaguard.shared.generated.resources.insulin
import diaguard.shared.generated.resources.meal
import diaguard.shared.generated.resources.oxygen_saturation
import diaguard.shared.generated.resources.pulse
import diaguard.shared.generated.resources.weight

class LocalizeMeasurementCategoryUseCase(
    private val localization: Localization,
) {

    operator fun invoke(category: MeasurementCategory.Local): MeasurementCategory.Localized {
        return MeasurementCategory.Localized(
            local = category,
            name = category.name
                .takeIf(String::isNotBlank)
                ?: category.key?.let { key ->
                    localization.getString(
                        when (key) {
                            DatabaseKey.MeasurementCategory.BLOOD_SUGAR -> Res.string.blood_sugar
                            DatabaseKey.MeasurementCategory.INSULIN -> Res.string.insulin
                            DatabaseKey.MeasurementCategory.MEAL -> Res.string.meal
                            DatabaseKey.MeasurementCategory.ACTIVITY -> Res.string.activity
                            DatabaseKey.MeasurementCategory.HBA1C -> Res.string.hba1c
                            DatabaseKey.MeasurementCategory.WEIGHT -> Res.string.weight
                            DatabaseKey.MeasurementCategory.PULSE -> Res.string.pulse
                            DatabaseKey.MeasurementCategory.BLOOD_PRESSURE -> Res.string.blood_pressure
                            DatabaseKey.MeasurementCategory.OXYGEN_SATURATION -> Res.string.oxygen_saturation
                        }
                    )
                } ?: "",
        )
    }
}