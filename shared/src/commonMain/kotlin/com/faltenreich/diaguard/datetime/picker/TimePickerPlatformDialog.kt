@file:JvmName("TimePickerPlatformDialogCommon")

package com.faltenreich.diaguard.datetime.picker

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.jvm.JvmName

@Composable
expect fun TimePickerPlatformDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
)

@Preview
@Composable
private fun Preview() = AppPreview {
    TimePickerPlatformDialog(
        onDismissRequest = {},
        confirmButton = {},
    ) {
        Text("TimePickerPlatformDialog")
    }
}