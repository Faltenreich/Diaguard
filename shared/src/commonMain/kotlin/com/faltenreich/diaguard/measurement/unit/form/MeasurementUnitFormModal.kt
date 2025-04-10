package com.faltenreich.diaguard.measurement.unit.form

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.modal.Modal
import com.faltenreich.diaguard.shared.di.viewModel

object MeasurementUnitFormModal : Modal {

    @Composable
    override fun Content() {
        MeasurementUnitForm(viewModel = viewModel())
    }
}