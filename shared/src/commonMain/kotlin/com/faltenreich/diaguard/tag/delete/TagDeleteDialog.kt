package com.faltenreich.diaguard.tag.delete

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString

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
                Text(getString(MR.strings.delete))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = { viewModel.dispatchIntent(TagDeleteIntent.Close) }) {
                Text(getString(MR.strings.cancel))
            }
        },

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