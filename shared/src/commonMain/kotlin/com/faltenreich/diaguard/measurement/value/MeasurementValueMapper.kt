package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import kotlinx.coroutines.flow.first

class MeasurementValueMapper(
    private val formatNumber: NumberFormatter,
    private val localization: Localization,
    private val getPreference: GetPreferenceUseCase,
) {

    suspend operator fun invoke(value: MeasurementValue): MeasurementValueForUser {
        val unit = value.property.selectedUnit
        return MeasurementValueForUser(
            value = formatNumber(
                number = value.value * unit.factor,
                // FIXME: Observe Flow
                scale = getPreference(DecimalPlaces).first(),
                locale = localization.getLocale(),
            ),
            unit = unit,
        )
    }

    suspend operator fun invoke(value: MeasurementValueForDatabase): MeasurementValueForUser {
        val unit = value.unit
        return MeasurementValueForUser(
            value = formatNumber(
                number = value.value * unit.factor,
                // FIXME: Observe Flow
                scale = getPreference(DecimalPlaces).first(),
                locale = localization.getLocale(),
            ),
            unit = unit,
        )
    }

    operator fun invoke(value: MeasurementValueForUser): MeasurementValueForDatabase? {
        val number = value.value.toDoubleOrNull() ?: return null
        val unit = value.unit
        return MeasurementValueForDatabase(value = number / unit.factor, unit = unit)
    }
}