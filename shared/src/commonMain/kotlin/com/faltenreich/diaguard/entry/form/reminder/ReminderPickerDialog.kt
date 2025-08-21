package com.faltenreich.diaguard.entry.form.reminder

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.datetime.picker.TimePickerDialog
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.reminder
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ReminderPickerDialog(
    time: Time,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (Time) -> Unit,
) {
    TimePickerDialog(
        time = time,
        onDismissRequest = onDismissRequest,
        onConfirmRequest = onConfirmRequest,
        title = stringResource(Res.string.reminder),
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    ReminderPickerDialog(
        time = now().time,
        onDismissRequest = {},
        onConfirmRequest = {},
    )
}