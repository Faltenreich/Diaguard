package com.faltenreich.diaguard.dashboard.hba1c

import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.format
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hba1c_estimated
import diaguard.shared.generated.resources.hba1c_latest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class GetCurrentHbA1cUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val getPreference: GetPreferenceUseCase,
    private val localization: Localization,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
    private val measurementValueMapper: MeasurementValueMapper,
) {

    operator fun invoke(): Flow<DashboardState.HbA1c?> {
        val propertyKey = DatabaseKey.MeasurementProperty.HBA1C
        val now = dateTimeFactory.now()
        return combine(
            getPreference(DecimalPlaces),
            // TODO: Avoid redundant call to property
            flowOf(propertyRepository.getByKey(DatabaseKey.MeasurementProperty.HBA1C.key)),
            valueRepository.observeLatestByProperty(propertyKey),
            valueRepository.observeAverageByPropertyKey(
                propertyKey = propertyKey,
                minDateTime = now.date.minus(1, DateUnit.QUARTER).atStartOfDay(),
                maxDateTime = now,
            )
        ) { decimalPlaces, property, latestHbA1c, averageOfQuarter ->
            when (latestHbA1c) {
                null -> DashboardState.HbA1c(
                    label = localization.getString(Res.string.hba1c_estimated),
                    value = averageOfQuarter?.let {
                        measurementValueMapper(
                            value = averageOfQuarter,
                            unit = property.selectedUnit,
                            decimalPlaces = decimalPlaces,
                        )
                    }
                )
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