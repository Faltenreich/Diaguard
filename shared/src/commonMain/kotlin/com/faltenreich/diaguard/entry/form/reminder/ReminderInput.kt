package com.faltenreich.diaguard.entry.form.reminder

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.form.EntryFormState
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_warning
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ReminderInput(
    state: EntryFormState.Reminder,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = state.label,
            modifier = Modifier.weight(1f),
        )
        if (state.showMissingPermissionInfo) {
            ResourceIcon(
                icon = Res.drawable.ic_warning,
                tint = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    ReminderInput(
        state = EntryFormState.Reminder(
            delayInMinutes = 10,
            label = "In 10 Minutes",
            showMissingPermissionInfo = false,
        ),
    )
}