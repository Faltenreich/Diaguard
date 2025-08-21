package com.faltenreich.diaguard.entry.form.reminder

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.form.EntryFormState
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ReminderPicker(
    state: EntryFormState.Reminder.Picker.PermissionGranted,
    onConfirmRequest: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var delayInMinutes by remember { mutableStateOf(state.delayInMinutes) }
    Text("ReminderPicker")
}

@Preview
@Composable
private fun Preview() = AppPreview {
    ReminderPicker(
        state = EntryFormState.Reminder.Picker.PermissionGranted(
            delayInMinutes = 10,
        ),
        onConfirmRequest = {},
    )
}