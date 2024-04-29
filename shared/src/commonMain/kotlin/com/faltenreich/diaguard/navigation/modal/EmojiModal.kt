package com.faltenreich.diaguard.navigation.modal

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.view.BottomSheet
import com.faltenreich.diaguard.shared.view.EmojiPicker

class EmojiModal(
    private val onDismissRequest: () -> Unit,
    private val onEmojiPicked: (String) -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        BottomSheet(onDismissRequest = onDismissRequest) {
            EmojiPicker(onEmojiPicked = onEmojiPicked)
        }
    }
}