package com.faltenreich.diaguard.dashboard.reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.view.image.ResourceIcon
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.delete
import diaguard.shared.generated.resources.ic_alarm
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.reminder_delete_dialog_description
import diaguard.shared.generated.resources.reminder_delete_dialog_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DashboardReminder(
    state: DashboardReminderState?,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeletionConfirmationDialog by remember { mutableStateOf(false) }

    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(horizontal = AppTheme.dimensions.padding.P_3),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ResourceIcon(
                icon = Res.drawable.ic_alarm,
                modifier = Modifier.size(AppTheme.dimensions.size.ImageSmall),
            )
            Text(
                text = state?.text ?: "",
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = { showDeletionConfirmationDialog = true }) {
                ResourceIcon(
                    icon = Res.drawable.ic_delete,
                    modifier = Modifier.size(AppTheme.dimensions.size.ImageSmall),
                )
            }
        }
    }

    if (showDeletionConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showDeletionConfirmationDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeletionConfirmationDialog = false
                        onDelete()
                    },
                ) {
                    Text(stringResource(Res.string.delete))
                }
            },
            modifier = modifier,
            dismissButton = {
                TextButton(onClick = { showDeletionConfirmationDialog = false }) {
                    Text(stringResource(Res.string.cancel))
                }
            },
            title = { Text(stringResource(Res.string.reminder_delete_dialog_title)) },
            text = { Text(stringResource(Res.string.reminder_delete_dialog_description)) },
        )
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    DashboardReminder(
        state = DashboardReminderState(
            text = "Reminder in 5 minutes"
        ),
        onDelete = {},
    )
}