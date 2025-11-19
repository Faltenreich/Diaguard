package com.faltenreich.diaguard.shared.view

import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import com.faltenreich.diaguard.R

@Composable
actual fun EmojiPicker(
    onEmojiPick: (String) -> Unit,
    columns: Int,
    isDarkColorScheme: Boolean,
    modifier: Modifier,
) {
    AndroidView(
        factory = { context ->
            val style = if (isDarkColorScheme) R.style.EmojiPicker_Dark else R.style.EmojiPicker_Light
            EmojiPickerView(ContextThemeWrapper(context, style)).apply {
                emojiGridColumns = columns
                isNestedScrollingEnabled = true
                setOnEmojiPickedListener { item ->
                    onEmojiPick(item.emoji)
                }
            }
        },
        modifier = Modifier.fillMaxSize().then(modifier),
    )
}