package com.faltenreich.diaguard.measurement.category.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import com.faltenreich.diaguard.view.TextInput
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.view.rememberFocusRequester
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.create
import diaguard.shared.generated.resources.measurement_category
import diaguard.shared.generated.resources.name
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementCategoryFormDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (name: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = rememberFocusRequester(requestFocus = true)

    var name by rememberSaveable { mutableStateOf("") }

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
        title = { Text(stringResource(Res.string.measurement_category)) },
        text = {
            TextInput(
                input = name,
                onInputChange = { name = it },
                label = stringResource(Res.string.name),
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done ),
            )
        }
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    MeasurementCategoryFormDialog(
        onDismissRequest = {},
        onConfirmRequest = {},
    )
}