package com.faltenreich.diaguard.dashboard.hba1c

import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.dashboard.hba1c.info.HbA1cInfoBottomSheet
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.navigation.bottomsheet.OpenBottomSheetUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.format
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hba1c_estimated
import diaguard.shared.generated.resources.hba1c_latest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCurrentHbA1cUseCase(
    private val getLatestHbA1c: GetLatestHbA1cUseCase,
    private val getEstimatedHbA1c: GetEstimatedHbA1cUseCase,
    private val getPreference: GetPreferenceUseCase,
    private val localization: Localization,
    private val dateTimeFormatter: DateTimeFormatter,
    private val measurementValueMapper: MeasurementValueMapper,
    private val pushScreen: PushScreenUseCase,
    private val openBottomSheet: OpenBottomSheetUseCase,
) {

    operator fun invoke(): Flow<DashboardState.HbA1c> {
        return combine(
            getLatestHbA1c(),
            getEstimatedHbA1c(),
            getPreference(DecimalPlacesPreference),
        ) { latestHbA1c, estimatedHbA1c, decimalPlaces ->
            when {
                latestHbA1c != null -> getLatestHbA1c(latestHbA1c, decimalPlaces)
                estimatedHbA1c != null -> getEstimatedHbA1c(estimatedHbA1c, decimalPlaces)
                else -> null
            } ?: DashboardState.HbA1c(
                label = localization.getString(Res.string.hba1c_estimated),
                value = null,
                onClick = { openBottomSheet(HbA1cInfoBottomSheet) },
            )
        }
    }

    private fun getLatestHbA1c(
        value: MeasurementValue.Local,
        decimalPlaces: Int,
    ): DashboardState.HbA1c {
        return DashboardState.HbA1c(
            label = localization.getString(Res.string.hba1c_latest).format(
                dateTimeFormatter.formatDate(value.entry.dateTime.date)
            ),
            value = measurementValueMapper(value, decimalPlaces),
            onClick = { pushScreen(EntryFormScreen(entry = value.entry)) },
        )
    }

    private fun getEstimatedHbA1c(
        value: MeasurementValue.Average,
        decimalPlaces: Int,
    ): DashboardState.HbA1c {
        return DashboardState.HbA1c(
            label = localization.getString(Res.string.hba1c_estimated),
            value = measurementValueMapper(
                value = value.value,
                unit = value.property.selectedUnit,
                decimalPlaces = decimalPlaces,
            ),
            onClick = { openBottomSheet(HbA1cInfoBottomSheet) },
        )
    }
}