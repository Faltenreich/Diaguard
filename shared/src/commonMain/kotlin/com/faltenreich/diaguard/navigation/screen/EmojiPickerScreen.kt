package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.view.EmojiPicker

data class EmojiPickerScreen(
    private val onEmojiPicked: (String) -> Unit,
    private val columns: Int = 9,
) : Screen() {

    @Composable
    override fun Content() {
        EmojiPicker(
            onEmojiPicked = onEmojiPicked,
            columns = columns,
        )
    }
}