package com.faltenreich.diaguard.tag.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.rememberFocusRequester

@Composable
fun TagFormDialog(
    modifier: Modifier = Modifier,
    viewModel: TagFormViewModel = inject(),
) {
    val focusRequester = rememberFocusRequester(requestFocus = true)

    val state = viewModel.collectState()
    var name by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { viewModel.dispatchIntent(TagFormIntent.Close) },
        confirmButton = {
            TextButton(onClick = { viewModel.dispatchIntent(TagFormIntent.Submit(name)) }) {
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
                input = name,
                onInputChange = { name = it },
                label = getString(Res.string.name),
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                supportingText = { Text(state?.inputError ?: "") },
                isError = state?.inputError != null,
            )
        }
    )
}