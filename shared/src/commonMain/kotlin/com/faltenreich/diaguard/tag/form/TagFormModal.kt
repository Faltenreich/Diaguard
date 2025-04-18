package com.faltenreich.diaguard.tag.form

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.modal.Modal
import com.faltenreich.diaguard.shared.di.viewModel

// TODO: Convert into non-Modal, like MeasurementUnitFormDialog
data object TagFormModal : Modal {

    @Composable
    override fun Content() {
        TagFormDialog(viewModel = viewModel())
    }
}