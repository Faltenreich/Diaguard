package com.faltenreich.diaguard.entry.form.reminder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.form.EntryFormState
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.reminder
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ReminderPickerDialog(
    state: EntryFormState.Reminder.Picker,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var delayInMinutes by remember { mutableStateOf(state.delayInMinutes) }

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

                Text("ReminderPicker")
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    ReminderPickerDialog(
        state = EntryFormState.Reminder.Picker(
            delayInMinutes = 10,
            isPermissionGranted = true,
        ),
        onDismissRequest = {},
        onConfirmRequest = {},
    )
}