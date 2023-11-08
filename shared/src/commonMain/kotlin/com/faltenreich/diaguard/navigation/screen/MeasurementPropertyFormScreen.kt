package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyForm
import com.faltenreich.diaguard.shared.di.getViewModel
import org.koin.core.parameter.parametersOf

data class MeasurementPropertyFormScreen(val property: MeasurementProperty) : Screen() {

    @Composable
    override fun Content() {
        MeasurementPropertyForm(viewModel = getViewModel { parametersOf(property) })
    }
}