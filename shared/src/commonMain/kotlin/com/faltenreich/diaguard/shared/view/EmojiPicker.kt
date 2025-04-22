package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun EmojiPicker(
    onEmojiPicked: (String) -> Unit,
    columns: Int,
    isDarkColorScheme: Boolean,
    modifier: Modifier = Modifier,
)