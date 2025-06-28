package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.appModule
import org.koin.compose.KoinApplication

@Composable
fun AppPreview(
    isDarkColorScheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    KoinApplication(application = { modules(appModule()) }) {
        AppTheme(isDarkColorScheme = isDarkColorScheme) {
            content()
        }
    }
}