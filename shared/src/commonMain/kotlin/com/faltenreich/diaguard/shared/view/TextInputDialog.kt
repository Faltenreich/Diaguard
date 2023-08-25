package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun TextInputDialog(
    title: String,
    label: String,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var input by mutableStateOf("")
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Dialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { onConfirmRequest(input) }) {
                Text(stringResource(MR.strings.create))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(MR.strings.cancel))
            }
        },
        title = { Text(title) },
        text = {
            TextInput(
                input = input,
                onInputChange = { input = it },
                label = label,
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            )
        }
    )
}