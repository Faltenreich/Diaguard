package com.faltenreich.diaguard.shared.view

import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import com.faltenreich.diaguard.R
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.theme.ThemeViewModel

@Composable
actual fun EmojiPicker(
    onEmojiPicked: (String) -> Unit,
    columns: Int,
    modifier: Modifier,
) {
    val isDarkColorScheme = inject<ThemeViewModel>().isDarkColorScheme()
    val style = if (isDarkColorScheme) R.style.EmojiPicker_Dark else R.style.EmojiPicker_Light

    AndroidView(
        factory = { context ->
            EmojiPickerView(ContextThemeWrapper(context, style)).apply {
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