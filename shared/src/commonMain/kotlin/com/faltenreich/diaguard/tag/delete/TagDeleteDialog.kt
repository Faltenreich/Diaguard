package com.faltenreich.diaguard.tag.delete

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
fun TagDeleteDialog(
    modifier: Modifier = Modifier,
    viewModel: TagDeleteViewModel = inject(),
) {
    val state = viewModel.collectState()
    AlertDialog(
        onDismissRequest = { viewModel.dispatchIntent(TagDeleteIntent.Close) },
        confirmButton = {
            TextButton(onClick = { viewModel.dispatchIntent(TagDeleteIntent.Confirm) }) {
                Text(
                    text = getString(MR.strings.delete),
                    color = AppTheme.colors.scheme.error,
                )
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = { viewModel.dispatchIntent(TagDeleteIntent.Close) }) {
                Text(getString(MR.strings.cancel))
            }
        },
        icon = { ResourceIcon(MR.images.ic_delete) },
        title = { Text(getString(MR.strings.tag_delete)) },
        text = {
            when (state) {
                null -> Unit
                else -> Text(
                    getString(
                        MR.strings.tag_delete_description,
                        state.entryCount,
                        state.tag.name,
                    )
                )
            }
        }
    )
}