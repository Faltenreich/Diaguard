package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.modal.Modal

class DeleteModal(
    private val onDismissRequest: () -> Unit,
    private val onConfirmRequest: () -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        DeleteDialog(
            onDismissRequest = onDismissRequest,
            onConfirmRequest = onConfirmRequest,
        )
    }
}