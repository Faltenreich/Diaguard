package com.faltenreich.diaguard.navigation.modal

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.MR.strings.delete_description
import com.faltenreich.diaguard.MR.strings.delete_title
import dev.icerock.moko.resources.compose.stringResource

class DeleteModal(
    private val onDismissRequest: () -> Unit,
    private val onConfirm: () -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(
                        text = stringResource(MR.strings.delete),
                        color = AppTheme.colors.Red,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = stringResource(MR.strings.cancel),
                        color = AppTheme.colors.scheme.onPrimary,
                    )
                }
            },
            title = { Text(stringResource(delete_title)) },
            text = { Text(stringResource(delete_description)) },
        )
    }
}