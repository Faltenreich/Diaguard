package com.faltenreich.diaguard.navigation.modal

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormDialog

class MeasurementCategoryFormModal(
    private val onDismissRequest: () -> Unit,
    private val onConfirmRequest: (name: String) -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        MeasurementCategoryFormDialog(
            onDismissRequest = onDismissRequest,
            onConfirmRequest = onConfirmRequest,
        )
    }
}