package com.faltenreich.diaguard.measurement.type.form

import androidx.compose.foundation.layout.Column
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
import com.faltenreich.diaguard.shared.view.Dialog
import com.faltenreich.diaguard.shared.view.TextInput
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MeasurementTypeFormDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (typeName: String, unitName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var typeName by mutableStateOf("")
    var unitName by mutableStateOf("")

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { onConfirmRequest(typeName, unitName) }) {
                Text(stringResource(MR.strings.create))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(MR.strings.cancel))
            }
        },
        title = { Text(stringResource(MR.strings.measurement_type_new)) },
        text = {
            Column {
                TextInput(
                    input = typeName,
                    onInputChange = { typeName = it },
                    label = stringResource(MR.strings.name),
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                )
                TextInput(
                    input = unitName,
                    onInputChange = { unitName = it },
                    label = stringResource(MR.strings.measurement_unit),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    )
}