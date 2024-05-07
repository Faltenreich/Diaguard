package com.faltenreich.diaguard.navigation.modal

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormDialog

class MeasurementPropertyFormModal(
    private val onDismissRequest: () -> Unit,
    private val onConfirmRequest: (propertyName: String, unitName: String) -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        MeasurementPropertyFormDialog(
            onDismissRequest = onDismissRequest,
            onConfirmRequest = onConfirmRequest,
        )
    }
}