package com.faltenreich.diaguard.shared.view.preview

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.appModule
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
inline fun AppPreview(
    isDarkColorScheme: Boolean = false,
    crossinline content: @Composable PreviewScope.() -> Unit,
) {
    KoinApplication(application = { modules(appModule() + previewModule()) }) {
        AppTheme(isDarkColorScheme = isDarkColorScheme) {
            with(koinInject<PreviewScope>()) {
                content()
            }
        }
    }
}