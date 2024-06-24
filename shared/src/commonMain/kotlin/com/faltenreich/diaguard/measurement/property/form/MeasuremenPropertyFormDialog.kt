package com.faltenreich.diaguard.measurement.property.form

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
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.rememberFocusRequester

@Composable
fun MeasurementPropertyFormDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (propertyName: String, unitName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = rememberFocusRequester(requestFocus = true)

    var propertyName by rememberSaveable { mutableStateOf("") }
    var unitName by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { onConfirmRequest(propertyName, unitName) }) {
                Text(getString(Res.string.create))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(getString(Res.string.cancel))
            }
        },
        title = { Text(getString(Res.string.measurement_property_add)) },
        text = {
            Column {
                TextInput(
                    input = propertyName,
                    onInputChange = { propertyName = it },
                    label = getString(Res.string.name),
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                )
                TextInput(
                    input = unitName,
                    onInputChange = { unitName = it },
                    label = getString(Res.string.measurement_unit),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    )
}