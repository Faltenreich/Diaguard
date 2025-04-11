package com.faltenreich.diaguard.measurement.category.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.navigation.modal.Modal

class MeasurementCategoryFormModal(
    private val onDismissRequest: () -> Unit,
    private val onConfirmRequest: (name: String) -> Unit,
) : Modal {

    @Composable
    override fun Content(modifier: Modifier) {
        MeasurementCategoryFormDialog(
            onDismissRequest = onDismissRequest,
            onConfirmRequest = onConfirmRequest,
        )
    }
}