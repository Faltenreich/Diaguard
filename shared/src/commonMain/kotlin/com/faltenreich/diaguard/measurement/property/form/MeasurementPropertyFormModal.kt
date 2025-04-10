package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.modal.Modal

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