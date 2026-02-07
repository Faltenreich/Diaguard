package com.faltenreich.diaguard.view.overlay

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.cancel
import com.faltenreich.diaguard.resource.delete
import com.faltenreich.diaguard.resource.delete_confirm
import com.faltenreich.diaguard.resource.delete_description
import com.faltenreich.diaguard.resource.delete_title
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteDialog(
    severity: DeleteSeverity,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
) {
    val backgroundColor = severity.backgroundColor()
    val foregroundColor = severity.foregroundColor()
    val buttonColor = severity.buttonColor()

    var tapsToConfirm by remember { mutableStateOf(severity.tapsToConfirm) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    if (tapsToConfirm > 1) {
                        tapsToConfirm--
                    } else {
                        onConfirmRequest()
                    }
                },
            ) {
                Text(
                    text = when (tapsToConfirm) {
                        1 -> stringResource(Res.string.delete)
                        else -> stringResource(Res.string.delete_confirm, tapsToConfirm)
                    },
                    color = buttonColor,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = stringResource(Res.string.cancel),
                    color = buttonColor,
                )
            }
        },
        title = { Text(stringResource(Res.string.delete_title)) },
        text = { Text(stringResource(Res.string.delete_description)) },
        containerColor = backgroundColor,
        iconContentColor = foregroundColor,
        titleContentColor = foregroundColor,
        textContentColor = foregroundColor,
    )
}

enum class DeleteSeverity(
    val backgroundColor: @Composable () -> Color,
    val foregroundColor: @Composable () -> Color,
    val buttonColor: @Composable () -> Color,
    val tapsToConfirm: Int,
) {
    LOW(
        backgroundColor = { AppTheme.colors.scheme.background },
        foregroundColor = { AppTheme.colors.scheme.onBackground },
        buttonColor = { AppTheme.colors.scheme.primary },
        tapsToConfirm = 1,
    ),
    MEDIUM(
        backgroundColor = { AppTheme.colors.scheme.errorContainer },
        foregroundColor = { AppTheme.colors.scheme.onErrorContainer },
        buttonColor = { AppTheme.colors.scheme.onErrorContainer },
        tapsToConfirm = 1,
    ),
    HIGH(
        backgroundColor = { AppTheme.colors.scheme.errorContainer },
        foregroundColor = { AppTheme.colors.scheme.onErrorContainer },
        buttonColor = { AppTheme.colors.scheme.onErrorContainer },
        tapsToConfirm = 3,
    ),
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(DeleteSeverityPreviewParameterProvider::class)
    severity: DeleteSeverity,
) {
    DeleteDialog(
        severity = severity,
        onDismissRequest = {},
        onConfirmRequest = {},
    )
}

private class DeleteSeverityPreviewParameterProvider : PreviewParameterProvider<DeleteSeverity> {
    override val values = DeleteSeverity.entries.asSequence()
}