package com.faltenreich.diaguard.shared.view.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.appModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

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
    KoinApplication(application = { modules(appModule() + previewModule()) }) {
        AppTheme(isDarkColorScheme = isDarkColorScheme) {
            with(koinInject<PreviewScope>()) {
                val backgroundColor =
                    if (showBackground) AppTheme.colors.scheme.background
                    else Color.Transparent
                Box(modifier = modifier.background(backgroundColor)) {
                    content()
                }
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