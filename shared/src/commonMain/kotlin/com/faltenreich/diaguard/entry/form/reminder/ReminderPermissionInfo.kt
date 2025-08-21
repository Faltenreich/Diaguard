package com.faltenreich.diaguard.entry.form.reminder

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ReminderPermissionInfo(
    onPermissionRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text("We need the permission to post notification in order to remind you.")
        Button(
            onClick = onPermissionRequest,
        ) {
            Text("Grant permission")
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    ReminderPermissionInfo(
        onPermissionRequest = {},
    )
}