package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.navigation.modal.Modal

class DeleteModal(
    private val onDismissRequest: () -> Unit,
    private val onConfirmRequest: () -> Unit,
) : Modal {

    @Composable
    override fun Content(modifier: Modifier) {
        DeleteDialog(
            onDismissRequest = onDismissRequest,
            onConfirmRequest = onConfirmRequest,
        )
    }
}