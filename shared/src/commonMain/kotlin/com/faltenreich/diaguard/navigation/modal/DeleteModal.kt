package com.faltenreich.diaguard.navigation.modal

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.AppTheme
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.delete
import diaguard.shared.generated.resources.delete_description
import diaguard.shared.generated.resources.delete_title
import org.jetbrains.compose.resources.stringResource

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
                        text = stringResource(Res.string.delete),
                        color = AppTheme.colors.Red,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = stringResource(Res.string.cancel),
                        color = AppTheme.colors.scheme.onPrimary,
                    )
                }
            },
            title = { Text(stringResource(Res.string.delete_title)) },
            text = { Text(stringResource(Res.string.delete_description)) },
        )
    }
}