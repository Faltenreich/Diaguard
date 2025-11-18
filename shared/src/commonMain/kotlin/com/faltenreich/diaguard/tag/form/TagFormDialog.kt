package com.faltenreich.diaguard.tag.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.shared.view.rememberFocusRequester
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.create
import diaguard.shared.generated.resources.name
import diaguard.shared.generated.resources.tag
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TagFormDialog(
    error: String?,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (name: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }
    val focusRequester = rememberFocusRequester(requestFocus = true)

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = { onConfirmRequest(name) },
                enabled = name.isNotBlank(),
            ) {
                Text(stringResource(Res.string.create))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.cancel))
            }
        },
        title = { Text(stringResource(Res.string.tag)) },
        text = {
            TextInput(
                input = name,
                onInputChange = { input -> name = input },
                label = stringResource(Res.string.name),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                supportingText = error?.let { error -> { Text(error) } },
                isError = error != null,
            )
        }
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    TagFormDialog(
        error = null,
        onDismissRequest = {},
        onConfirmRequest = {},
    )
}