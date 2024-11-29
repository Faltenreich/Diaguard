package com.faltenreich.diaguard.dashboard.hba1c.info

import com.faltenreich.diaguard.dashboard.hba1c.GetCurrentHbA1cUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetEstimatedHbA1cUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetLatestHbA1cUseCase
import com.faltenreich.diaguard.dashboard.latest.GetLatestBloodSugarUseCase
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.tint.GetMeasurementValueTintUseCase
import com.faltenreich.diaguard.preference.Preference
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow

class HbA1cInfoViewModel(
    getLatestHbA1c: GetLatestHbA1cUseCase,
    getEstimatedHbA1c: GetEstimatedHbA1cUseCase,
    getPreference: GetPreferenceUseCase,
    valueMapper: MeasurementValueMapper,
    dateTimeFormatter: DateTimeFormatter,
    getValueColor: GetMeasurementValueTintUseCase,
) : ViewModel<HbA1cInfoState, Unit, Unit>() {

    override val state = combine(
        getPreference(DecimalPlacesPreference),
        getLatestHbA1c(),
        getEstimatedHbA1c(),
    ) { decimalPlaces, latestHbA1c, estimatedHbA1c ->
        HbA1cInfoState(
            latest = latestHbA1c?.let {
                HbA1cInfoState.Latest(
                    value = valueMapper(latestHbA1c, decimalPlaces).value,
                    dateTime = dateTimeFormatter.formatDateTime(latestHbA1c.entry.dateTime),
                    tint = getValueColor(latestHbA1c),
                )
            },
            estimated = estimatedHbA1c?.let {
                HbA1cInfoState.Estimated(
                    value = valueMapper(estimatedHbA1c.value, estimatedHbA1c.property.selectedUnit, decimalPlaces).value,
                    dateTimeRangeStart = "",
                    valueCount = 0,
                    tint = getValueColor(estimatedHbA1c),
                )
            },
        )
    }

    override suspend fun handleIntent(intent: Unit) {
        super.handleIntent(intent)
    }
}