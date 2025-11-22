package com.faltenreich.diaguard.data.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Convenience Composable that supports theming and fake data
 */
@Composable
inline fun PreviewScaffold(
    modifier: Modifier = Modifier,
    isDarkColorScheme: Boolean = false,
    crossinline content: @Composable PreviewScope.() -> Unit,
) {
    AppTheme(isDarkColorScheme = isDarkColorScheme) {
        with(PreviewScope()) {
            Box(modifier = modifier) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PreviewScaffold {
        Text("AppPreview")
    }
}