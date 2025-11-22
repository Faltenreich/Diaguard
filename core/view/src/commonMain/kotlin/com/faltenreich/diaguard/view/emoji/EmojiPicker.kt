@file:JvmName("EmojiPickerCommon")

package com.faltenreich.diaguard.view.emoji

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.jvm.JvmName

@Composable
expect fun EmojiPicker(
    onEmojiPick: (String) -> Unit,
    columns: Int,
    isDarkColorScheme: Boolean,
    modifier: Modifier = Modifier,
)

@Preview
@Composable
private fun Preview() {
    EmojiPicker(
        onEmojiPick = {},
        columns = 4,
        isDarkColorScheme = false,
    )
}