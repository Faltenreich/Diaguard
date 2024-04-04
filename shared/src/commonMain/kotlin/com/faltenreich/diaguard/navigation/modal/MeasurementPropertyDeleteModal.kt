package com.faltenreich.diaguard.navigation.modal

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.localization.getString

class MeasurementPropertyDeleteModal(
    private val onDismissRequest: () -> Unit,
    private val onConfirm: () -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(getString(MR.strings.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(getString(MR.strings.cancel))
                }
            },
            title = { Text(getString(MR.strings.measurement_property_delete)) },
            text = { Text(getString(MR.strings.measurement_property_delete_description)) },
        )
    }
}