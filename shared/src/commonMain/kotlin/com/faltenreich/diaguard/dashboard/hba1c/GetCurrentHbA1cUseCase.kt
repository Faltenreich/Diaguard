package com.faltenreich.diaguard.dashboard.hba1c

import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.format
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hba1c_latest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCurrentHbA1cUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val getPreference: GetPreferenceUseCase,
    private val localization: Localization,
    private val dateTimeFormatter: DateTimeFormatter,
    private val measurementValueMapper: MeasurementValueMapper,
) {

    operator fun invoke(): Flow<DashboardState.HbA1c?> {
        return combine(
            getPreference(DecimalPlaces),
            valueRepository.observeLatestByProperty(DatabaseKey.MeasurementProperty.HBA1C),
        ) { decimalPlaces, latestHbA1c ->
            when (latestHbA1c) {
                null -> null // TODO
                else -> DashboardState.HbA1c(
                    label = localization.getString(Res.string.hba1c_latest).format(
                        dateTimeFormatter.formatDate(latestHbA1c.entry.dateTime.date)
                    ),
                    value = measurementValueMapper(
                        value = latestHbA1c,
                        decimalPlaces = decimalPlaces,
                    )
                )
            }
        }
    }
}