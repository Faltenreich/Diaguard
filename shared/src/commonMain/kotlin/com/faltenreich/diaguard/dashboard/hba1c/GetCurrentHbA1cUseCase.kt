package com.faltenreich.diaguard.dashboard.hba1c

import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.dashboard.hba1c.info.HbA1cInfoScreen
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCurrentHbA1cUseCase(
    private val getLatestHbA1c: GetLatestHbA1cUseCase,
    private val getEstimatedHbA1c: GetEstimatedHbA1cUseCase,
    private val getPreference: GetPreferenceUseCase,
    private val measurementValueMapper: MeasurementValueMapper,
    private val pushScreen: PushScreenUseCase,
) {

    operator fun invoke(): Flow<DashboardState.HbA1c> {
        return combine(
            getLatestHbA1c(),
            getEstimatedHbA1c(),
            getPreference(DecimalPlacesPreference),
        ) { latestHbA1c, estimatedHbA1c, decimalPlaces ->
            DashboardState.HbA1c(
                estimation = estimatedHbA1c?.let {
                    measurementValueMapper(
                        value = estimatedHbA1c,
                        decimalPlaces = decimalPlaces,
                    )
                },
                latest = latestHbA1c?.let {
                    measurementValueMapper(
                        value = latestHbA1c.value,
                        unit = latestHbA1c.property.selectedUnit,
                        decimalPlaces = decimalPlaces,
                    )
                },
                onClick = { pushScreen(HbA1cInfoScreen) },
            )
        }
    }
}