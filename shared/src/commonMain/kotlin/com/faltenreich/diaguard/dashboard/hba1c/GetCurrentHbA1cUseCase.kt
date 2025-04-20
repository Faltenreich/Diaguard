package com.faltenreich.diaguard.dashboard.hba1c

import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCurrentHbA1cUseCase(
    private val getLatestHbA1c: GetLatestHbA1cUseCase,
    private val getEstimatedHbA1c: GetEstimatedHbA1cUseCase,
    private val getPreference: GetPreferenceUseCase,
    private val measurementValueMapper: MeasurementValueMapper,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(): Flow<DashboardState.HbA1c> {
        return combine(
            getLatestHbA1c(),
            getEstimatedHbA1c(),
            getPreference(DecimalPlacesPreference),
        ) { latestHbA1c, estimatedHbA1c, decimalPlaces ->
            when {
                latestHbA1c != null -> DashboardState.HbA1c.Latest(
                    entry = latestHbA1c.entry,
                    dateTime = dateTimeFormatter.formatDate(latestHbA1c.entry.dateTime.date),
                    value = measurementValueMapper(latestHbA1c, decimalPlaces),
                )
                estimatedHbA1c != null -> DashboardState.HbA1c.Estimated(
                    value = measurementValueMapper(
                        value = estimatedHbA1c.value,
                        property = estimatedHbA1c.property,
                        decimalPlaces = decimalPlaces,
                    ),
                )
                else -> DashboardState.HbA1c.Unknown
            }
        }
    }
}