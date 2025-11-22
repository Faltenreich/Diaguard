package com.faltenreich.diaguard.entry.form.reminder

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.permission_grant
import diaguard.shared.generated.resources.reminder
import diaguard.shared.generated.resources.reminder_permission_rationale
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ReminderPermissionDialog(
    onDismissRequest: () -> Unit,
    onPermissionRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onPermissionRequest) {
                Text(stringResource(Res.string.permission_grant))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.cancel))
            }
        },
        title = { Text(stringResource(Res.string.reminder)) },
        text = { Text(stringResource(Res.string.reminder_permission_rationale)) },
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    ReminderPermissionDialog(
        onDismissRequest = {},
        onPermissionRequest = {},
    )
}