package com.faltenreich.diaguard.entry.form.reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.form.EntryFormState
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.ok
import diaguard.shared.generated.resources.reminder
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun ReminderPickerDialog(
    state: EntryFormState.Reminder.Picker,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (Duration) -> Unit,
) {
    var duration by remember { mutableStateOf(state.duration) }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = AlertDialogDefaults.shape,
            colors = CardDefaults.cardColors(
                containerColor = AlertDialogDefaults.containerColor,
                contentColor = AlertDialogDefaults.textContentColor,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = AlertDialogDefaults.TonalElevation,
            ),
        ) {
            Column(
                modifier = Modifier.padding(AppTheme.dimensions.padding.P_3_5),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
            ) {
                Text(
                    text = getString(Res.string.reminder),
                    style = AppTheme.typography.labelLarge,
                )

                ReminderPicker(
                    duration = state.duration,
                    onChange = { duration = it },
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(stringResource(Res.string.cancel))
                    }
                    TextButton(onClick = { onConfirmRequest(duration) }) {
                        Text(stringResource(Res.string.ok))
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
        state = EntryFormState.Reminder.Picker(
            duration = 10.minutes,
            isPermissionGranted = true,
        ),
        onDismissRequest = {},
        onConfirmRequest = {},
    )
}