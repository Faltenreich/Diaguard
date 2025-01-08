package com.faltenreich.diaguard.shared.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme

@Composable
fun NoticeBar(
    text: String,
    isVisible: Boolean,
    style: NoticeBarStyle,
    slideInFromBottom: Boolean = true,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { if (slideInFromBottom) it else it / 2 }),
        exit = slideOutVertically(targetOffsetY = { if (slideInFromBottom) it else it / 2 }),
    ) {
        Text(
            text = text,
            modifier = modifier
                .background(
                    when (style) {
                        NoticeBarStyle.WARNING -> AppTheme.colors.YellowDark
                        NoticeBarStyle.ERROR -> AppTheme.colors.scheme.errorContainer
                    }
                )
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimensions.padding.P_3,
                    vertical = AppTheme.dimensions.padding.P_2,
                ),
            color = when (style) {
                NoticeBarStyle.WARNING -> AppTheme.colors.scheme.onBackground
                NoticeBarStyle.ERROR -> AppTheme.colors.scheme.onErrorContainer
            }
        )
    }
}