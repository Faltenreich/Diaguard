package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
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
import com.faltenreich.diaguard.shared.view.TextInput
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MeasurementPropertyFormDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (propertyName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var propertyName by mutableStateOf("")

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { onConfirmRequest(propertyName) }) {
                Text(stringResource(MR.strings.create))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(MR.strings.cancel))
            }
        },
        title = { Text(stringResource(MR.strings.measurement_property_new)) },
        text = {
            TextInput(
                input = propertyName,
                onInputChange = { propertyName = it },
                label = stringResource(MR.strings.name),
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            )
        }
    )
}