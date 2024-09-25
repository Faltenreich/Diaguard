package com.faltenreich.diaguard.tag.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.rememberFocusRequester
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.create
import diaguard.shared.generated.resources.name
import diaguard.shared.generated.resources.tag

@Composable
fun TagFormDialog(
    modifier: Modifier = Modifier,
    viewModel: TagFormViewModel = inject(),
) {
    val focusRequester = rememberFocusRequester(requestFocus = true)

    AlertDialog(
        onDismissRequest = { viewModel.dispatchIntent(TagFormIntent.Close) },
        confirmButton = {
            TextButton(onClick = { viewModel.dispatchIntent(TagFormIntent.Submit) }) {
                Text(getString(Res.string.create))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = { viewModel.dispatchIntent(TagFormIntent.Close) }) {
                Text(getString(Res.string.cancel))
            }
        },
        title = { Text(getString(Res.string.tag)) },
        text = {
            TextInput(
                input = viewModel.name,
                onInputChange = { input ->
                    viewModel.name = input
                    viewModel.error = null
                },
                label = getString(Res.string.name),
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                supportingText = viewModel.error?.let { error -> { Text(error) } },
                isError = viewModel.error != null,
            )
        }
    )
}