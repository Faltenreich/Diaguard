package com.faltenreich.diaguard.datetime

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun TimePickerPlatformDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier,
    dismissButton: @Composable (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit,
) {
    TODO("Not yet implemented")
}