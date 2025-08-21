package com.faltenreich.diaguard.entry.form.reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.form.EntryFormState
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.ok
import diaguard.shared.generated.resources.reminder
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ReminderPickerDialog(
    state: EntryFormState.Reminder.Picker,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (Int?) -> Unit,
    onPermissionRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(AppTheme.dimensions.padding.P_3),
            ) {
                Text(getString(Res.string.reminder))

                when (state) {
                    is EntryFormState.Reminder.Picker.PermissionGranted -> ReminderPicker(
                        state = state,
                        onConfirmRequest = onConfirmRequest,
                    )
                    is EntryFormState.Reminder.Picker.PermissionDenied -> ReminderPermissionInfo(
                        onPermissionRequest = onPermissionRequest,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(getString(Res.string.cancel))
                    }
                    TextButton(onClick = { onConfirmRequest(null) }) {
                        Text(getString(Res.string.ok))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    ReminderPickerDialog(
        state = EntryFormState.Reminder.Picker.PermissionGranted(
            delayInMinutes = 10,
        ),
        onDismissRequest = {},
        onConfirmRequest = {},
        onPermissionRequest = {},
    )
}