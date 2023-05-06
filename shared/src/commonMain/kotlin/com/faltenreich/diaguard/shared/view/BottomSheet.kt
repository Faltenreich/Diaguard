package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
expect fun BottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: BottomSheetState = remember { BottomSheetState() },
    content: @Composable () -> Unit,
)