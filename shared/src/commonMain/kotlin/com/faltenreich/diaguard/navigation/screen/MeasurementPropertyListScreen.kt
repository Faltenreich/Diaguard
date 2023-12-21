package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyList
import com.faltenreich.diaguard.shared.di.getViewModel

data object MeasurementPropertyListScreen : Screen {

    @Composable
    override fun Content() {
        MeasurementPropertyList(viewModel = getViewModel())
    }
}