package com.faltenreich.diaguard.shared.view

import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView

@Composable
actual fun EmojiPicker(
    onEmojiPicked: (String) -> Unit,
    columns: Int,
    modifier: Modifier,
) {
    AndroidView(
        factory = { context ->
            EmojiPickerView(context).apply {
                emojiGridColumns = columns
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    // FIXME: Breaks scrolling of container
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                setOnEmojiPickedListener { item ->
                    onEmojiPicked(item.emoji)
                }
            }
        },
        modifier = modifier.fillMaxWidth(),
    )
}