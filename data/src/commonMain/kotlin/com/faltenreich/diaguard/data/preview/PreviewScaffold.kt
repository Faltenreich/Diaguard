package com.faltenreich.diaguard.data.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.fake.FakeFactory
import com.faltenreich.diaguard.view.theme.AppTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Convenience Composable that supports theming and fake data
 */
@Composable
inline fun PreviewScaffold(
    modifier: Modifier = Modifier,
    isDarkColorScheme: Boolean = false,
    crossinline content: @Composable FakeFactory.() -> Unit,
) {
    AppTheme(isDarkColorScheme = isDarkColorScheme) {
        with(FakeFactory) {
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