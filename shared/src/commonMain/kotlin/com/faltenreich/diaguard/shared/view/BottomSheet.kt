package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun BottomSheet(
    // FIXME: Not called when pulled down
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: BottomSheetState = remember { BottomSheetState() },
    content: @Composable () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState.delegate,
        content = { content() },
    )
}