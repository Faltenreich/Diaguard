package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.modal.Modal

class EmojiModal(
    private val onDismissRequest: () -> Unit,
    private val onEmojiPicked: (String) -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        ModalBottomSheet(onDismissRequest = onDismissRequest) {
            EmojiPicker(onEmojiPicked = onEmojiPicked)
        }
    }
}