package com.faltenreich.diaguard.shared.view.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Convenience Composable that supports theming and dependency injection
 */
@Composable
inline fun AppPreview(
    modifier: Modifier = Modifier,
    showBackground: Boolean = true,
    isDarkColorScheme: Boolean = false,
    crossinline content: @Composable PreviewScope.() -> Unit,
) {
    AppTheme(isDarkColorScheme = isDarkColorScheme) {
        with(PreviewScope()) {
            val backgroundColor =
                if (showBackground) AppTheme.colors.scheme.background
                else Color.Transparent
            Box(modifier = modifier.background(backgroundColor)) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppPreview {
        Text("AppPreview")
    }
}