package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun EmojiPicker(
    onEmojiPicked: (String) -> Unit,
    columns: Int = 9,
    modifier: Modifier = Modifier,
)