package com.faltenreich.diaguard.tag.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun TagFormDialog(
    modifier: Modifier = Modifier,
    viewModel: TagFormViewModel = inject(),
) {
    val state = viewModel.collectState()
    var name by rememberSaveable { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    AlertDialog(
        onDismissRequest = { viewModel.dispatchIntent(TagFormIntent.Close) },
        confirmButton = {
            TextButton(onClick = { viewModel.dispatchIntent(TagFormIntent.Submit(name)) }) {
                Text(getString(MR.strings.create))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = { viewModel.dispatchIntent(TagFormIntent.Close) }) {
                Text(getString(MR.strings.cancel))
            }
        },
        title = { Text(getString(MR.strings.tag)) },
        text = {
            TextInput(
                input = name,
                onInputChange = { name = it },
                label = getString(MR.strings.name),
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                supportingText = { Text(state?.inputError ?: "") },
                isError = state?.inputError != null,
            )
        }
    )
}