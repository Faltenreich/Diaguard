package com.faltenreich.diaguard.dashboard.latest

import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.usecase.GetMeasurementValueTintUseCase
import com.faltenreich.diaguard.data.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.GetPreferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetDashboardLatestUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val valueMapper: MeasurementValueMapper,
    private val getValueColor: GetMeasurementValueTintUseCase,
    private val getPreference: GetPreferenceUseCase,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(): Flow<DashboardLatestState> {
        val propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR
        return combine(
            valueRepository.observeLatestByProperty(key = propertyKey),
            getPreference(DecimalPlacesPreference),
        ) { value, decimalPlaces ->
            when (value) {
                null -> DashboardLatestState.None
                else -> DashboardLatestState.Value(
                    entry = value.entry,
                    value = valueMapper(
                        value = value,
                        decimalPlaces = decimalPlaces,
                    ),
                    tint = getValueColor(value),
                    timePassed = dateTimeFormatter.formatTimePassed(
                        start = value.entry.dateTime,
                        end = dateTimeFactory.now(),
                    ),
                )
            }
        }
    }
}