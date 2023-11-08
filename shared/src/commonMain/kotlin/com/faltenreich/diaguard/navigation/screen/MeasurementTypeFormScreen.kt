package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeForm
import com.faltenreich.diaguard.shared.di.getViewModel
import org.koin.core.parameter.parametersOf

data class MeasurementTypeFormScreen(val measurementTypeId: Long) : Screen() {

    @Composable
    override fun Content() {
        MeasurementTypeForm(viewModel = getViewModel { parametersOf(measurementTypeId) })
    }
}