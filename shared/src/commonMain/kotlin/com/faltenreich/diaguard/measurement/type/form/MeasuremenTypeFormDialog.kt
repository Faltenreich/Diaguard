package com.faltenreich.diaguard.measurement.type.form

import androidx.compose.foundation.layout.Column
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
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.rememberFocusRequester

@Composable
fun MeasurementTypeFormDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (typeName: String, unitName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = rememberFocusRequester(requestFocus = true)

    var typeName by rememberSaveable { mutableStateOf("") }
    var unitName by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { onConfirmRequest(typeName, unitName) }) {
                Text(getString(MR.strings.create))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(getString(MR.strings.cancel))
            }
        },
        title = { Text(getString(MR.strings.measurement_type_add)) },
        text = {
            Column {
                TextInput(
                    input = typeName,
                    onInputChange = { typeName = it },
                    label = getString(MR.strings.name),
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                )
                TextInput(
                    input = unitName,
                    onInputChange = { unitName = it },
                    label = getString(MR.strings.measurement_unit),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    )
}