package com.faltenreich.diaguard.dashboard.hba1c

import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.navigation.bar.snack.ShowSnackbarUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateToScreenUseCase
import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.format
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hba1c_estimated
import diaguard.shared.generated.resources.hba1c_formula
import diaguard.shared.generated.resources.hba1c_latest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCurrentHbA1cUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val getPreference: GetPreferenceUseCase,
    private val localization: Localization,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
    private val measurementValueMapper: MeasurementValueMapper,
    private val navigateToScreen: NavigateToScreenUseCase,
    private val showSnackbar: ShowSnackbarUseCase,
) {

    operator fun invoke(): Flow<DashboardState.HbA1c?> {
        val now = dateTimeFactory.now()
        return combine(
            getPreference(DecimalPlaces),
            valueRepository.observeLatestByProperty(DatabaseKey.MeasurementProperty.HBA1C),
            valueRepository.observeAverageByPropertyKey(
                propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                minDateTime = now.date.minus(1, DateUnit.QUARTER).atStartOfDay(),
                maxDateTime = now,
            ),
        ) { decimalPlaces, latestHbA1c, averageBloodSugar ->
            when {
                latestHbA1c != null -> DashboardState.HbA1c(
                    label = localization.getString(Res.string.hba1c_latest).format(
                        dateTimeFormatter.formatDate(latestHbA1c.entry.dateTime.date)
                    ),
                    value = measurementValueMapper(
                        value = latestHbA1c,
                        decimalPlaces = decimalPlaces,
                    ),
                    onClick = {
                        navigateToScreen(EntryFormScreen(entry = latestHbA1c.entry))
                    },
                )
                averageBloodSugar?.value != null -> DashboardState.HbA1c(
                    label = localization.getString(Res.string.hba1c_estimated),
                    value = measurementValueMapper(
                        value = 0.031 * averageBloodSugar.value + 2.393,
                        unit = averageBloodSugar.property.selectedUnit,
                        decimalPlaces = decimalPlaces,
                    ),
                    onClick = {
                        showSnackbar.invoke(localization.getString(Res.string.hba1c_formula))
                    },
                )
                else -> null
            }
        }
    }
}