package com.faltenreich.diaguard.datetime.picker

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier,
    dismissButton: @Composable (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit,
) {
    TODO("Not yet implemented")
}