package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun BottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier,
    sheetState: BottomSheetState,
    content: @Composable () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(),
        content = { content() },
    )
}