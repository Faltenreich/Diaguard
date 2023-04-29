package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun BottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier,
    sheetState: BottomSheetState,
    content: @Composable ColumnScope.() -> Unit,
) {
    TODO()
}