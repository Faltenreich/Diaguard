package com.faltenreich.diaguard.shared.view

import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
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
            // FIXME: Apply colorControlNormal and colorAccent
            EmojiPickerView(context).apply {
                emojiGridColumns = columns
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    // FIXME: Breaks scrolling of container
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                isNestedScrollingEnabled = true
                setOnEmojiPickedListener { item ->
                    onEmojiPicked(item.emoji)
                }
            }
        },
        modifier = modifier.fillMaxSize(),
    )
}