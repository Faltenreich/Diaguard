package com.faltenreich.diaguard.view.overlay

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.view.theme.AppTheme
import diaguard.core.view.generated.resources.Res
import diaguard.core.view.generated.resources.cancel
import diaguard.core.view.generated.resources.delete
import diaguard.core.view.generated.resources.delete_description
import diaguard.core.view.generated.resources.delete_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        // TODO: Double-check confirmation if deletion is far-reaching, e.g. for MeasurementCategory
        confirmButton = {
            TextButton(onClick = onConfirmRequest) {
                Text(
                    text = stringResource(Res.string.delete),
                    color = AppTheme.colors.scheme.onErrorContainer,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = stringResource(Res.string.cancel),
                    color = AppTheme.colors.scheme.onErrorContainer,
                )
            }
        },
        title = { Text(stringResource(Res.string.delete_title)) },
        text = { Text(stringResource(Res.string.delete_description)) },
        containerColor = AppTheme.colors.scheme.errorContainer,
        iconContentColor = AppTheme.colors.scheme.onErrorContainer,
        titleContentColor = AppTheme.colors.scheme.onErrorContainer,
        textContentColor = AppTheme.colors.scheme.onErrorContainer,
    )
}

@Preview
@Composable
private fun Preview() {
    DeleteDialog(
        onDismissRequest = {},
        onConfirmRequest = {},
    )
}