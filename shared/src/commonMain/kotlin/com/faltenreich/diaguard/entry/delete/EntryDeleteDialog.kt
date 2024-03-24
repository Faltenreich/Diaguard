package com.faltenreich.diaguard.entry.delete

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.ResourceIcon

@Composable
fun EntryDeleteDialog(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryDeleteViewModel = inject(),
) {
    AlertDialog(
        onDismissRequest = { viewModel.dispatchIntent(EntryDeleteIntent.Close) },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.dispatchIntent(EntryDeleteIntent.Confirm)
                    onConfirm()
                },
            ) {
                Text(
                    text = getString(MR.strings.delete),
                    color = AppTheme.colors.scheme.error,
                )
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = { viewModel.dispatchIntent(EntryDeleteIntent.Close) }) {
                Text(getString(MR.strings.cancel))
            }
        },
        icon = { ResourceIcon(MR.images.ic_delete) },
        title = { Text(getString(MR.strings.entry_delete)) },
        // TODO: Display measurement count (must be lazily loaded before)
        text = { Text(getString(MR.strings.entry_delete_description)) }
    )
}