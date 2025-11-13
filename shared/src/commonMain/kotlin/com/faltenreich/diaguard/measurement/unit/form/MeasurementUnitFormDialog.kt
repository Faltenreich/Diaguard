package com.faltenreich.diaguard.measurement.unit.form

import androidx.compose.foundation.layout.Column
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
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.core.localization.getString
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.abbreviation
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.create
import diaguard.shared.generated.resources.measurement_unit
import diaguard.shared.generated.resources.name
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementUnitFormDialog(
    unit: MeasurementUnit.Local?,
    error: String?,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (name: String, abbreviation: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf(unit?.name ?: "") }
    var abbreviation  by remember { mutableStateOf(unit?.abbreviation ?: "") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = { onConfirmRequest(name, abbreviation) },
                enabled = name.isNotBlank(),
            ) {
                Text(getString(Res.string.create))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(getString(Res.string.cancel))
            }
        },
        title = { Text(getString(Res.string.measurement_unit)) },
        text = {
            Column {
                TextInput(
                    input = name,
                    onInputChange = { name = it },
                    label = getString(Res.string.name),
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = error?.let { error -> { Text(error) } },
                    isError = error != null,
                )
                TextInput(
                    input = abbreviation,
                    onInputChange = { abbreviation = it },
                    label = getString(Res.string.abbreviation),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    MeasurementUnitFormDialog(
        unit = unit(),
        error = null,
        onDismissRequest = {},
        onConfirmRequest = { _, _ -> },
    )
}