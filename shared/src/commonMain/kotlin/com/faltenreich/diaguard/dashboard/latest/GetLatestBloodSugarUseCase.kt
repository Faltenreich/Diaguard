package com.faltenreich.diaguard.dashboard.latest

import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.tint.GetMeasurementValueTintUseCase
import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetLatestBloodSugarUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val valueMapper: MeasurementValueMapper,
    private val getValueColor: GetMeasurementValueTintUseCase,
    private val getPreference: GetPreferenceUseCase,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(): Flow<DashboardState.LatestBloodSugar?> {
        val dateTime = dateTimeFactory.now()
        return combine(
            valueRepository.observePreviousByProperty(
                dateTime = dateTime,
                key = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
            ),
            getPreference(DecimalPlaces),
        ) { value, decimalPlaces ->
            when (value) {
                null -> null
                else -> DashboardState.LatestBloodSugar(
                    entry = value.entry,
                    value = valueMapper(
                        value = value,
                        decimalPlaces = decimalPlaces,
                    ),
                    tint = getValueColor(value),
                    timePassed = dateTimeFormatter.formatTimePassed(
                        start = value.entry.dateTime,
                        end = dateTime,
                    ),
                )
            }
        }
    }
}