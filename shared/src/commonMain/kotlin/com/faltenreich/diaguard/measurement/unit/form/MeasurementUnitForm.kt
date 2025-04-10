package com.faltenreich.diaguard.measurement.unit.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.rememberFocusRequester
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.abbreviation
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.create
import diaguard.shared.generated.resources.measurement_unit
import diaguard.shared.generated.resources.name

@Composable
fun MeasurementUnitForm(
    viewModel: MeasurementUnitFormViewModel,
    modifier: Modifier = Modifier,
) {
    val focusRequester = rememberFocusRequester(requestFocus = true)
    val onDismissRequest = { viewModel.dispatchIntent(MeasurementUnitFormIntent.Close) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { viewModel.dispatchIntent(MeasurementUnitFormIntent.Submit) }) {
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
                    input = viewModel.name,
                    onInputChange = { viewModel.name = it },
                    label = getString(Res.string.name),
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                )
                TextInput(
                    input = viewModel.abbreviation,
                    onInputChange = { viewModel.abbreviation = it },
                    label = getString(Res.string.abbreviation),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    )
}