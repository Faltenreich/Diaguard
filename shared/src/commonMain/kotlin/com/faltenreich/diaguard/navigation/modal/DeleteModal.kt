package com.faltenreich.diaguard.navigation.modal

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.delete
import diaguard.shared.generated.resources.delete_description
import diaguard.shared.generated.resources.delete_title

class DeleteModal(
    private val onDismissRequest: () -> Unit,
    private val onConfirmRequest: () -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onConfirmRequest) {
                    Text(
                        text = getString(Res.string.delete),
                        color = AppTheme.colors.Red,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = getString(Res.string.cancel),
                        color = AppTheme.colors.scheme.onPrimary,
                    )
                }
            },
            title = { Text(getString(Res.string.delete_title)) },
            text = { Text(getString(Res.string.delete_description)) },
        )
    }
}